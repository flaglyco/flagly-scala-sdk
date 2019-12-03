package co.flagly

import co.flagly.core.Flag
import co.flagly.utils.JsonUtils
import dev.akif.e.{E, Maybe}
import dev.akif.e.syntax._
import io.circe.Decoder
import sttp.client.circe.asJson
import sttp.client.{DeserializationError, HttpError, NothingT, SttpBackend, UriContext, basicRequest}
import sttp.model.{HeaderNames, StatusCode}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.control.NonFatal

class Flagly(val host: String, val token: String)(implicit ec: ExecutionContext, sttp: SttpBackend[Future, Nothing, NothingT]) {
  def use[A](name: String)(ifDisabled: => A)(ifEnabled: => A): Future[Maybe[A]] =
    getFlag(name).map { flag =>
      if (flag.value()) {
        ifEnabled.maybe
      } else {
        ifDisabled.maybe
      }
    }.recoverWith {
      case e: E        => Future.successful(e.maybe[A])
      case NonFatal(t) => Future.successful(E.of(StatusCode.InternalServerError.code, "flagly", s"Cannot check flag '$name'", t).maybe)
    }

  private implicit val flagDecoder: Decoder[Flag] = Decoder.decodeJson.emapTry(json => Try(JsonUtils.fromJson[Flag](json.noSpaces, classOf[Flag])))

  private def getFlag(name: String): Future[Flag] =
    basicRequest
      .header(HeaderNames.Authorization, s"Bearer $token")
      .get(uri"$host/flags/$name")
      .response(asJson[Flag])
      .send()
      .map { response =>
        val e = E.of(StatusCode.InternalServerError.code, "flagly", s"Cannot check flag '$name'")

        response.body match {
          case Left(HttpError(res))               => throw e.data("reason", "Flagly API returned invalid response").data("response", res)
          case Left(DeserializationError(res, _)) => throw e.data("reason", "Cannot parse response as Flag").data("response", res)
          case Right(flag)                        => flag
        }
      }
}
