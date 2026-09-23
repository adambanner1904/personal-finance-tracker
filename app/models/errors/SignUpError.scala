package models.errors

enum SignUpError:
  case InvalidEmail(error: ParseEmailError)
  case EmailAlreadyUsed
