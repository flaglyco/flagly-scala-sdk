package co.flagly

import java.util.UUID

import co.flagly.core.{Flag, FlaglyError}
import co.flagly.utils.ZDT
import play.api.libs.json.{JsError, JsObject, JsSuccess, Reads}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.control.NonFatal

class Flagly(config: SDKConfig, http: Http) {
  def getFlag(id: UUID)(implicit ec: ExecutionContext): Future[Option[Flag]] =
    http.get(s"${config.host}/flags/$id").map(_.asOpt[Flag]).recoverWith {
      case NonFatal(t) =>
        Future.failed(FlaglyError.of(503, s"Cannot get flag $id!", t))
    }

  implicit val flagReads: Reads[Flag] =
    Reads[Flag] {
      case json: JsObject =>
        val maybeFlag = for {
          id          <- (json \ "id").asOpt[UUID]
          name        <- (json \ "name").asOpt[String]
          description <- (json \ "description").asOpt[String]
          value       <- (json \ "value").asOpt[Boolean]
          createdAt   <- (json \ "createdAt").asOpt[String].flatMap(zdt => Try(ZDT.fromString(zdt)).toOption)
          updatedAt   <- (json \ "updatedAt").asOpt[String].flatMap(zdt => Try(ZDT.fromString(zdt)).toOption)
        } yield {
          Flag.of(id, name, description, value, createdAt, updatedAt)
        }

        maybeFlag match {
          case None       => JsError(s"$json is not a valid Flag!")
          case Some(flag) => JsSuccess(flag)
        }

      case json =>
        JsError(s"$json is not a valid Flag!")
    }
}
