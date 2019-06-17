package co.flagly.data

import org.scalatest.{Matchers, WordSpec}

class FlagSpec extends WordSpec with Matchers {
  "A Flag" should {
    "have correct DataType and value class" when {
      "it is a BooleanFlag" in {
        booleanFlag.dataType       shouldBe DataType.Boolean
        classOf[booleanFlag.Value] shouldBe classOf[Boolean]
      }

      "it is a NumberFlag" in {
        numberFlag.dataType       shouldBe DataType.Number
        classOf[numberFlag.Value] shouldBe classOf[BigDecimal]
      }

      "it is a TextFlag" in {
        textFlag.dataType       shouldBe DataType.Text
        classOf[textFlag.Value] shouldBe classOf[String]
      }
    }
  }

  private lazy val booleanFlag = Flag.boolean("", "", value = false)
  private lazy val numberFlag  = Flag.number("", "", 0)
  private lazy val textFlag    = Flag.text("", "", "")
}
