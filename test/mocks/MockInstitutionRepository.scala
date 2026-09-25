package mocks

import org.typelevel.doobie.free.connection.pure
import models.{Institution, Session}
import persistence.InstitutionRepository
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar

trait MockInstitutionRepository extends MockitoSugar:
  
  val mockInstitutionRepo = mock[InstitutionRepository]

  def mockFindByName(name: String)(toReturn: Option[Institution])(using session: Session): Unit =
    when(mockInstitutionRepo.findByName(name)).thenReturn(pure(toReturn))

  def mockInsert(name: String)(using session: Session): Unit =
    when(mockInstitutionRepo.insert(name)).thenReturn(pure(()))

  def mockList(institutions: Institution*)(using session: Session): Unit =
    when(mockInstitutionRepo.list()).thenReturn(pure(institutions.toList))