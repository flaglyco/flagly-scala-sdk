package co.flagly

import java.util.UUID

import co.flagly.core.{Flag, FlaglyError}
import co.flagly.core.FlagJson.flagReads
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class Flagly(config: SDKConfig, http: Http) {
  def getFlag(id: UUID)(implicit ec: ExecutionContext): Future[Option[Flag]]     = internalGetFlag(http.get(s"${config.host}/flags/$id"))
  def getFlag(name: String)(implicit ec: ExecutionContext): Future[Option[Flag]] = internalGetFlag(http.get(s"${config.host}/flags?name=$name"))

  def useFlag[A](id: UUID)(action: Flag => A)(implicit ec: ExecutionContext): Future[Option[A]]     = internalUseFlag(getFlag(id), action)
  def useFlag[A](name: String)(action: Flag => A)(implicit ec: ExecutionContext): Future[Option[A]] = internalUseFlag(getFlag(name), action)

  def isFlagEnabled(id: UUID, default: => Boolean)(implicit ec: ExecutionContext): Future[Boolean]     = internalIsFlagEnabled(getFlag(id), default)
  def isFlagEnabled(name: String, default: => Boolean)(implicit ec: ExecutionContext): Future[Boolean] = internalIsFlagEnabled(getFlag(name), default)

  private def internalGetFlag(future: Future[JsValue])(implicit ec: ExecutionContext): Future[Option[Flag]] =
    future.map(_.asOpt[Flag]).recoverWith {
      case NonFatal(t) =>
        Future.failed(FlaglyError.of(s"Cannot get flag!", t))
    }

  private def internalUseFlag[A](future: Future[Option[Flag]], action: Flag => A)(implicit ec: ExecutionContext): Future[Option[A]] =
    future.map(_.map(action)).recoverWith {
      case NonFatal(t) =>
        Future.failed(FlaglyError.of(s"Cannot use flag!", t))
    }

  private def internalIsFlagEnabled(future: Future[Option[Flag]], default: => Boolean)(implicit ec: ExecutionContext): Future[Boolean] =
    future.map(_.map(f => f.value).getOrElse(default)).recoverWith {
      case NonFatal(_) =>
        Future.successful(default)

      case t =>
        Future.failed(FlaglyError.of(s"Cannot check if flag is enabled!", t))
    }
}
