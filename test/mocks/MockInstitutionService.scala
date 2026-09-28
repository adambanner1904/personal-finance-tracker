package mocks

import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.*
import services.InstitutionService
import models.errors.AddInstitutionError
import models.Institution

trait MockInstitutionService extends MockitoSugar:
  
  val mockInstitutionService = mock[InstitutionService]

  def mockListInstitutions(userId: Long, institutions: Institution*): Unit =
    when(mockInstitutionService.listInstitutions(userId)).thenReturn(institutions.toList)

  def mockAddInstitution(userId: Long, name: String)(response: Either[AddInstitutionError, Unit]): Unit =
    when(mockInstitutionService.addInstitution(name, userId)).thenReturn(response)

  def mockFindById(id: Long, userId: Long)(response: Option[Institution]): Unit =
    when(mockInstitutionService.findById(id, userId)).thenReturn(response)

  def mockRenameInstitution(institutionId: Long, userId: Long)(newName: String): Unit =
    doNothing().when(mockInstitutionService).renameInstitution(institutionId, userId)(newName)