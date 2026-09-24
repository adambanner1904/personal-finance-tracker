package persistence

import action.UserRequest
import models.{Institution, Time}

import javax.inject.{Inject, Singleton}

import org.typelevel.doobie.ConnectionIO
import org.typelevel.doobie.implicits.*

@Singleton
class InstitutionRepository @Inject():
  def list()(using request: UserRequest[?]): ConnectionIO[List[Institution]] =
    sql"""
        select * 
        from institutions 
        where user_id = ${request.userSession.userId}
        order by name
    """.query[Institution].to[List]

  def findById(id: Long)(using request: UserRequest[?]): ConnectionIO[Option[Institution]] =
    sql"""
        select * 
        from institutions 
        where id = ${id} 
          and user_id = ${request.userSession.userId}
    """.query[Institution].option

  def insert(name: String)(using request: UserRequest[?]): ConnectionIO[Long] =
    sql"""
        insert into institutions (user_id, name) 
        values (${request.userSession.userId}, ${name})
        returning id
    """.query[Long].unique

  def archive(institutionId: Long)(using request: UserRequest[?]): ConnectionIO[Time] =
    sql"""
        update institutions set archived_at = ${Time.now} 
        where id = ${institutionId} 
          and user_id = ${request.userSession.userId}
        returning archived_at
    """.query[Time].unique

  def unarchive(institutionId: Long)(using request: UserRequest[?]): ConnectionIO[Long] =
    sql"""
        update institutions set archived_at = null 
        where id = ${institutionId} 
        and user_id = ${request.userSession.userId}
        returning id 
    """.query[Long].unique

  def rename(institutionId: Long, newName: String)(using request: UserRequest[?]): ConnectionIO[Long] =
    sql"""
        update institutions set name = ${newName} 
        where id = ${institutionId} 
          and user_id = ${request.userSession.userId}
        returning id
    """.query[Long].unique
