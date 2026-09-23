package persistence

import org.typelevel.doobie.ConnectionIO
import action.UserRequest
import models.{Institution, Time}
import org.typelevel.doobie.implicits.*


@javax.inject.Singleton
class InstitutionRepository @javax.inject.Inject(): 
  def getAllInstitutions()(using request: UserRequest[?]): ConnectionIO[List[Institution]] = 
    sql"select * from institutions where user_id = ${request.userSession.userId}"
      .query[Institution]
      .to[List]

  def findInstitutionById(id: Long)(using request: UserRequest[?]): ConnectionIO[Option[Institution]] = 
    sql"select * from institutions where id = ${id} and user_id = ${request.userSession.userId}"
      .query[Institution]
      .option

  def insertInstitution(name: String)(using request: UserRequest[?]): ConnectionIO[Long] = 
    sql"insert into institutions (user_id, name) values (${request.userSession.userId}, ${name})"
      .update
      .withUniqueGeneratedKeys[Long]("id")

  def archiveInstitution(institutionId: Long, archivedAt: Time)(using request: UserRequest[?]): ConnectionIO[Long] = 
    sql"""
        update institutions set archived_at = ${archivedAt} 
        where id = ${institutionId} 
          and user_id = ${request.userSession.userId}
        returning id
    """
      .query[Long]
      .unique

  def unarchiveInstitution(institutionId: Long)(using request: UserRequest[?]): ConnectionIO[Long] = 
    sql"""
        update institutions set archived_at = null 
        where id = ${institutionId} 
        and user_id = ${request.userSession.userId}
        returning id 
    """
      .query[Long]
      .unique
  