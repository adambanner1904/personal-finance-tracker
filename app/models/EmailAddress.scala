package models

import org.typelevel.doobie.util.meta.Meta

opaque type EmailAddress = String

object EmailAddress:
  enum Error:
    case Empty, TooLong, InvalidFormat

  private val MaxLength = 320
  private val EmailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$".r

  def from(value: String): Either[Error, EmailAddress] =
    val normalised = value.toLowerCase.trim
    if normalised.isBlank then Left(Error.Empty)
    else if normalised.length > MaxLength then Left(Error.TooLong)
    else if !EmailRegex.matches(normalised) then Left(Error.InvalidFormat)
    else Right(normalised)

  def unsafeFrom(value: String): EmailAddress = value

  extension (email: EmailAddress)
    def value: String = email

  given Meta[EmailAddress] = Meta.StringMeta.imap(_.value)(unsafeFrom)

  