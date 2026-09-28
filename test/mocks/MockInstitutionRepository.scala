package mocks

import org.typelevel.doobie.free.connection.pure
import models.Institution
import persistence.InstitutionRepository
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar

trait MockInstitutionRepository extends MockitoSugar:
  
  val mockInstitutionRepo = mock[InstitutionRepository]

  def mockFindByName(name: String, userId: Long)(toReturn: Option[Institution]): Unit =
    when(mockInstitutionRepo.findByName(name, userId)).thenReturn(pure(toReturn))

  def mockInsert(name: String, userId: Long): Unit =
    when(mockInstitutionRepo.insert(name, userId)).thenReturn(pure(()))

  def mockList(userId: Long, institutions: Institution*): Unit =
    when(mockInstitutionRepo.list(userId)).thenReturn(pure(institutions.toList))

  def mockRename(institutionId: Long, userId: Long)(newName: String): Unit =
    when(mockInstitutionRepo.rename(institutionId, userId)(newName)).thenReturn(pure(()))