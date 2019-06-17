package co.flagly.core

trait Encoder[-A] { self =>
  def encode(input: A): String

  def contraMap[B](f: B => A): Encoder[B] = (input: B) => self.encode(f(input))
}

object Encoder {
  def apply[A](implicit encoder: Encoder[A]): Encoder[A] = encoder

  implicit class syntax[A](val input: A) {
    def encode(implicit encoder: Encoder[A]): String = encoder.encode(input)
  }
}
