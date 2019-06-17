package co.flagly.core

sealed abstract class DataType(val name: String)

object DataType {
  final case object Boolean extends DataType("boolean")
  final case object Number  extends DataType("number")
  final case object Text    extends DataType("text")

  def byName(name: String): Option[DataType] =
    name match {
      case Boolean.name => Some(Boolean)
      case Number.name  => Some(Number)
      case Text.name    => Some(Text)
      case _            => None
    }
}
