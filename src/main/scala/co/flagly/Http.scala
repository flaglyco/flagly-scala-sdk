package co.flagly

import co.flagly.core.FlaglyError
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend
import com.softwaremill.sttp.playJson.asJson
import com.softwaremill.sttp.{SttpApi, SttpBackend, emptyRequest => http}
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}

class Http extends SttpApi {
  implicit val sttpBackend: SttpBackend[Future, Nothing] = AsyncHttpClientFutureBackend()

  def get(url: String)(implicit ec: ExecutionContext): Future[JsValue] =
    http
      .get(uri"$url")
      .response(asJson[JsValue])
      .send().flatMap { response =>
        response.body match {
          case Left(error)        => Future.failed(FlaglyError.of(503, error))
          case Right(Left(error)) => Future.failed(FlaglyError.of(503, error.message))
          case Right(Right(js))   => Future.successful(js)
        }
      }
}
