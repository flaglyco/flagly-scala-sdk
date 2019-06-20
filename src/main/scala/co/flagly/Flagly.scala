package co.flagly

import java.util.UUID

import co.flagly.core.{Flag, FlaglyError}

import scala.concurrent.{ExecutionContext, Future}

class Flagly(config: SDKConfig) {
  def getFlag(id: UUID)(implicit ec: ExecutionContext): Future[Option[Flag]] = Future.failed(FlaglyError.of("Not implemented yet!"))
}
