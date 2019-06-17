package co.flagly.codec

import co.flagly.codec.Decoder.syntax
import org.scalatest.{Matchers, WordSpec}

import scala.util.Try

class DecoderSpec extends WordSpec with Matchers {
  "A simple Decoder" should {
    "fail to decode an invalid value" in {
      Decoder[Int].decode("a") shouldBe None
      "a".decode               shouldBe None
    }

    "decode a value by summoning" in {
      Decoder[Int].decode("5") shouldBe Some(5)
    }

    "decode a value by syntax" in {
      "5".decode[Int] shouldBe Some(5)
    }
  }

  "An Decoder built by mapping an existing Decoder" should {
    implicit lazy val fooDecoder: Decoder[Foo] = intDecoder.map[Foo](Foo.apply)

    "decode a value by summoning" in {
      Decoder[Foo].decode("3") shouldBe Some(Foo(3))
    }

    "decode a value by syntax" in {
      "3".decode[Foo] shouldBe Some(Foo(3))
    }
  }

  "An Decoder built by flatMapping an existing Decoder" should {
    implicit lazy val fooDecoder: Decoder[Foo] = intDecoder.flatMap[Foo](i => Some(Foo(i)))

    "decode a value by summoning" in {
      Decoder[Foo].decode("3") shouldBe Some(Foo(3))
    }

    "decode a value by syntax" in {
      "3".decode[Foo] shouldBe Some(Foo(3))
    }
  }

  private case class Foo(a: Int)

  implicit lazy val intDecoder: Decoder[Int] = (input: String) => Try(input.toInt).toOption
}
