package models.errors

enum ParseEmailError:
  case EmptyEmail
  case EmailTooLong
  case InvalidEmailFormat

object ParseEmailError:
  extension (err: ParseEmailError)
    def message: String = err match
      case ParseEmailError.EmptyEmail         => "Email field cannot be left blank"
      case ParseEmailError.EmailTooLong       => "Email field must be less than 320 characters"
      case ParseEmailError.InvalidEmailFormat => "Email format is invalid"
