package co.flagly.codec

import co.flagly.codec.Encoder.syntax
import org.scalatest.{Matchers, WordSpec}

class EncoderSpec extends WordSpec with Matchers {
  "A simple Encoder" should {
    "encode a value by summoning" in {
      Encoder[Int].encode(5) shouldBe "5"
    }

    "encode a value by syntax" in {
      5.encode shouldBe "5"
    }
  }

  "An Encoder built by contramapping an existing Encoder" should {
    implicit lazy val fooEncoder: Encoder[Foo] = intEncoder.contraMap[Foo](_.a)

    "encode a value by summoning" in {
      Encoder[Foo].encode(Foo(3)) shouldBe "3"
    }

    "encode a value by syntax" in {
      Foo(3).encode shouldBe "3"
    }
  }

  private case class Foo(a: Int)

  implicit lazy val intEncoder: Encoder[Int] = (input: Int) => input.toString
}
