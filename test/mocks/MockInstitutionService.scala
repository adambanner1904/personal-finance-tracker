package mocks

import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.when
import services.InstitutionService

trait MockInstitutionService extends MockitoSugar:
  
  val mockInstitutionService = mock[InstitutionService]

  def mockListInstitutions(userId: Long, institutions: String*): Unit =
    when(mockInstitutionService.listInstitutions(userId)).thenReturn(institutions.toList)