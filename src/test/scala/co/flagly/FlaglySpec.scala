package co.flagly

import java.util.UUID

import co.flagly.core.Flag
import co.flagly.utils.JsonUtils
import dev.akif.e.E
import dev.akif.e.syntax._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import sttp.client.testing.SttpBackendStub
import sttp.client.{NothingT, Response, SttpBackend}
import sttp.model.StatusCode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class FlaglySpec extends AnyWordSpec with Matchers {
  "A Flagly instance" must {
    "have host of Flagly API and application token configured" in {
      implicit val sttp = getSttp
      val flagly = getFlagly

      flagly.host  mustBe "test-host"
      flagly.token mustBe "test-token"
    }
  }

  "Using a Flagly instance" must {
    "fail when getting a flag fails" in {
      val e = E
        .of(500, "flagly", s"Cannot check flag 'test'")
        .data("reason", "Flagly API returned invalid response")
        .data("response", "Flagly API is down")

      implicit val sttp = getSttp
        .whenAnyRequest
        .thenRespond(Response("Flagly API is down", StatusCode.InternalServerError))

      val flagly = getFlagly

      val future = flagly.use[String]("test") {
        "flag is disabled"
      } {
        "flag is enabled"
      }

      val result = Await.result(future, Duration.Inf)

      result mustBe e.maybe
    }

    "perform disabled action when flag is disabled" in {
      val flag = Flag.of(UUID.randomUUID(), "test", "Test flag", false)

      implicit val sttp = getSttp
        .whenRequestMatches(_.uri.path.endsWith(List("flags", "test")))
        .thenRespond(Response(JsonUtils.toJson[Flag](flag), StatusCode.Ok))

      val flagly = getFlagly

      val future = flagly.use[String]("test") {
        "flag is disabled"
      } {
        "flag is enabled"
      }

      val result = Await.result(future, Duration.Inf)

      result mustBe "flag is disabled".maybe
    }

    "perform enabled action when flag is enabled" in {
      val flag = Flag.of(UUID.randomUUID(), "test", "Test flag", true)

      implicit val sttp = getSttp
        .whenRequestMatches(_.uri.path.endsWith(List("flags", "test")))
        .thenRespond(Response(JsonUtils.toJson[Flag](flag), StatusCode.Ok))

      val flagly = getFlagly

      val future = flagly.use[String]("test") {
        "flag is disabled"
      } {
        "flag is enabled"
      }

      val result = Await.result(future, Duration.Inf)

      result mustBe "flag is enabled".maybe
    }
  }

  private def getSttp: SttpBackendStub[Future, Nothing] = SttpBackendStub.asynchronousFuture

  private def getFlagly(implicit sttp: SttpBackendStub[Future, Nothing]): Flagly = new Flagly("test-host", "test-token")
}
