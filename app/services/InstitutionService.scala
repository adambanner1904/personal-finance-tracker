package services

import models.errors.AddInstitutionError

import persistence.InstitutionRepository

import javax.inject.{Inject, Singleton}
import models.Session
import implicits.Repository.*
import models.db.Transactor

@Singleton
class InstitutionService @Inject (implicit
  val xa: Transactor,
  institutionRepo: InstitutionRepository,
):
  def addInstitution(name: String)(using
    session: Session,
  ): Either[AddInstitutionError, Unit] =
    institutionRepo.findByName(name).execute match
      case Some(_) => Left(AddInstitutionError.InstitutionAlreadyExists(name))
      case None    =>
        institutionRepo.insert(name).execute
        Right(())
