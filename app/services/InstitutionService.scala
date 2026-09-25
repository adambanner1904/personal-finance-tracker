package services

import models.errors.AddInstitutionError

import persistence.InstitutionRepository

import javax.inject.{Inject, Singleton}
import implicits.Repository.*
import models.db.Transactor

@Singleton
class InstitutionService @Inject (implicit
  val xa: Transactor,
  institutionRepo: InstitutionRepository,
):
  def listInstitutions(userId: Long): List[String] =
    institutionRepo.list(userId).execute.map(_.name)
    
  def addInstitution(name: String, userId: Long): Either[AddInstitutionError, Unit] =
    institutionRepo.findByName(name, userId).execute match
      case Some(_) => Left(AddInstitutionError.InstitutionAlreadyExists(name))
      case None    =>
        institutionRepo.insert(name, userId).execute
        Right(())
