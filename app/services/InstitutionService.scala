package services

import models.errors.AddInstitutionError

import persistence.InstitutionRepository

import javax.inject.{Inject, Singleton}
import implicits.Repository.*
import models.db.Transactor
import models.Institution

@Singleton
class InstitutionService @Inject (implicit
  val xa: Transactor,
  institutionRepo: InstitutionRepository,
):
  def listInstitutions(userId: Long): List[Institution] =
    institutionRepo.list(userId).execute

  def findById(id: Long, userId: Long): Option[Institution] =
    institutionRepo.findById(id, userId).execute
    
  def addInstitution(name: String, userId: Long): Either[AddInstitutionError, Unit] =
    institutionRepo.findByName(name, userId).execute match
      case Some(_) => Left(AddInstitutionError.InstitutionAlreadyExists(name))
      case None    =>
        institutionRepo.insert(name, userId).execute
        Right(())

  def renameInstitution(institutionId: Long, userId: Long)(newName: String): Unit = 
    institutionRepo.rename(institutionId, userId)(newName).execute

  def archiveInstitution(institutionId: Long, userId: Long): String =
    institutionRepo.archive(institutionId, userId).execute
    