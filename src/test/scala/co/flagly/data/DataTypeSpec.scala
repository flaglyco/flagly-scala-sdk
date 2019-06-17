package co.flagly.data

import org.scalatest.{Matchers, WordSpec}

class DataTypeSpec extends WordSpec with Matchers {
  "Building DataType by name" should {
    "fail for invalid name" in {
      DataType.byName("foo") shouldBe None
    }

    "return `Boolean` for 'boolean'" in {
      DataType.byName("boolean") shouldBe Some(DataType.Boolean)
    }

    "return `Number` for 'number'" in {
      DataType.byName("number") shouldBe Some(DataType.Number)
    }

    "return `Text` for 'text'" in {
      DataType.byName("text") shouldBe Some(DataType.Text)
    }
  }
}
