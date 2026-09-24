package models.errors

enum AddInstitutionError:
  case InstitutionAlreadyExists(name: String)

object AddInstitutionError:
  extension (e: AddInstitutionError)
    def message: String = e match
      case InstitutionAlreadyExists(name) =>
        s"An institution with the name '$name' already exists."
