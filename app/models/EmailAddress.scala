package models

import models.errors.*
import models.errors.ParseEmailError.*

import org.typelevel.doobie.util.meta.Meta

opaque type EmailAddress = String

object EmailAddress:
  private val MaxLength  = 320
  private val EmailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$".r

  def from(value: String): Either[ParseEmailError, EmailAddress] =
    val normalised = value.toLowerCase.trim
    if normalised.isBlank then Left(EmptyEmail)
    else if normalised.length > MaxLength then Left(EmailTooLong)
    else if !EmailRegex.matches(normalised) then Left(InvalidEmailFormat)
    else Right(normalised)

  def unsafeFrom(value: String): EmailAddress = value

  extension (email: EmailAddress) def value: String = email

  given Meta[EmailAddress] = Meta.StringMeta.imap(_.value)(unsafeFrom)
