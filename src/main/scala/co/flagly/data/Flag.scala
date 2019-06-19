package co.flagly.data

import java.time.ZonedDateTime
import java.util.UUID

final case class Flag(id: UUID,
                      name: String,
                      description: String,
                      value: Boolean,
                      createdAt: ZonedDateTime,
                      updatedAt: ZonedDateTime)

object Flag {
  def apply(name: String, description: String, value: Boolean): Flag =
    Flag(
      id           = UUID.randomUUID,
      name         = name,
      description  = description,
      value        = value,
      createdAt    = ZonedDateTime.now,
      updatedAt    = ZonedDateTime.now
    )
}
