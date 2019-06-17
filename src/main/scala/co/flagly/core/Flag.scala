package co.flagly.core

import java.time.ZonedDateTime
import java.util.UUID

sealed trait Flag {
  type Value

  val id: UUID
  val name: String
  val description: String
  val dataType: DataType
  val value: Value
  val createdAt: ZonedDateTime
  val updatedAt: ZonedDateTime
}

object Flag {
  def boolean(name: String, description: String, value: Boolean): BooleanFlag =
    BooleanFlag(
      id           = UUID.randomUUID,
      name         = name,
      description  = description,
      value        = value,
      createdAt    = ZonedDateTime.now,
      updatedAt    = ZonedDateTime.now
    )

  def number(name: String, description: String, value: BigDecimal): NumberFlag =
    NumberFlag(
      id           = UUID.randomUUID,
      name         = name,
      description  = description,
      value        = value,
      createdAt    = ZonedDateTime.now,
      updatedAt    = ZonedDateTime.now
    )

  def text(name: String, description: String, value: String): TextFlag =
    TextFlag(
      id           = UUID.randomUUID,
      name         = name,
      description  = description,
      value        = value,
      createdAt    = ZonedDateTime.now,
      updatedAt    = ZonedDateTime.now
    )
}

final case class BooleanFlag(override val id: UUID,
                             override val name: String,
                             override val description: String,
                             override val value: Boolean,
                             override val createdAt: ZonedDateTime,
                             override val updatedAt: ZonedDateTime) extends Flag {
  override type Value = Boolean

  override val dataType: DataType = DataType.Boolean
}

final case class NumberFlag(override val id: UUID,
                            override val name: String,
                            override val description: String,
                            override val value: BigDecimal,
                            override val createdAt: ZonedDateTime,
                            override val updatedAt: ZonedDateTime) extends Flag {
  override type Value = BigDecimal

  override val dataType: DataType = DataType.Number
}

final case class TextFlag(override val id: UUID,
                          override val name: String,
                          override val description: String,
                          override val value: String,
                          override val createdAt: ZonedDateTime,
                          override val updatedAt: ZonedDateTime) extends Flag {
  override type Value = String

  override val dataType: DataType = DataType.Text
}
