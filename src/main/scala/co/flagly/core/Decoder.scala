package co.flagly.core

trait Decoder[+A] { self =>
  def decode(input: String): Option[A]

  def map[B](f: A => B): Decoder[B] = flatMap(o => Option(f(o)))

  def flatMap[B](f: A => Option[B]): Decoder[B] = { input: String => self.decode(input).flatMap(f) }
}

object Decoder {
  def apply[A](implicit decoder: Decoder[A]): Decoder[A] = decoder

  implicit class syntax[A](val input: String) {
    def decode(implicit decoder: Decoder[A]): Option[A] = decoder.decode(input)
  }
}
