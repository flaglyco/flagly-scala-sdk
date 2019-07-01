package co.flagly

import co.flagly.core.{Flag, FlaglyError}
import co.flagly.utils.JsonUtils
import play.api.libs.json.{JsError, JsSuccess, Reads}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.control.NonFatal

class Flagly(config: SDKConfig, http: Http) {
  implicit val flagReads: Reads[Flag] = Reads[Flag] { json =>
    Try(JsonUtils.fromJson[Flag](json.toString(), classOf[Flag])).fold(
      t    => JsError(s"$json is not a valid Flag! ${t.getMessage}"),
      flag => JsSuccess(flag)
    )
  }

  def getFlag(name: String)(implicit ec: ExecutionContext): Future[Option[Flag]] =
    http
      .get(s"${config.host}/flags/$name", config.token)
      .map(_.asOpt[Flag])
      .recoverWith {
        case NonFatal(t) =>
          Future.failed(FlaglyError.of(s"Cannot get flag!", t))
      }

  def feature[A](name: String, default: Boolean)(enabledAction: => Future[A])(disabledAction: => Future[A])(implicit ec: ExecutionContext): Future[A] =
    getFlag(name).flatMap { flagOpt =>
      val enabled = flagOpt.map(_.value).getOrElse(default)

      if (enabled) {
        enabledAction
      } else {
        disabledAction
      }
    }.recoverWith {
      case NonFatal(t) =>
        Future.failed(FlaglyError.of(s"Cannot use flag!", t))
    }

  def featureWithFailure[A](name: String, default: Boolean)(action: => Future[A])(implicit ec: ExecutionContext): Future[A] =
    feature(name, default)(action) {
      throw FlaglyError.of("Flag is disabled!")
    }

  def isFlagEnabled(name: String, default: => Boolean)(implicit ec: ExecutionContext): Future[Boolean] =
    getFlag(name).map { flagOpt =>
      flagOpt.map(_.value).getOrElse(default)
    }.recoverWith {
      case NonFatal(_) =>
        Future.successful(default)

      case t =>
        Future.failed(FlaglyError.of(s"Cannot check if flag is enabled!", t))
    }
}
