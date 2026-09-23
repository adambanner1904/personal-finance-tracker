package models.errors

enum LoginError:
  case InvalidEmail(error: ParseEmailError)
  case InvalidCredentials
