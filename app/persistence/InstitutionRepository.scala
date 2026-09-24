package persistence

import models.{Institution, Session, Time}

import javax.inject.{Inject, Singleton}

import org.typelevel.doobie.ConnectionIO
import org.typelevel.doobie.implicits.*

@Singleton
class InstitutionRepository @Inject():
  def list()(using session: Session): ConnectionIO[List[Institution]] =
    sql"""
        select * 
        from institutions 
        where user_id = ${session.userId}
        order by name
    """.query[Institution].to[List]

  def findById(id: Long)(using session: Session): ConnectionIO[Option[Institution]] =
    sql"""
        select * 
        from institutions 
        where id = $id
          and user_id = ${session.userId}
    """.query[Institution].option
    
  def findByName(name: String)(using session: Session): ConnectionIO[Option[Institution]] =
    sql"""
        select * 
        from institutions 
        where name = $name
          and user_id = ${session.userId}
    """.query[Institution].option
    
  def insert(name: String)(using session: Session): ConnectionIO[Long] =
    sql"""
        insert into institutions (user_id, name) 
        values (${session.userId}, ${name})
        returning id
    """.query[Long].unique

  def archive(institutionId: Long)(using session: Session): ConnectionIO[Time] =
    sql"""
        update institutions set archived_at = ${Time.now} 
        where id = ${institutionId} 
          and user_id = ${session.userId}
        returning archived_at
    """.query[Time].unique

  def unarchive(institutionId: Long)(using session: Session): ConnectionIO[Long] =
    sql"""
        update institutions set archived_at = null 
        where id = ${institutionId} 
        and user_id = ${session.userId}
        returning id 
    """.query[Long].unique

  def rename(institutionId: Long, newName: String)(using session: Session): ConnectionIO[Long] =
    sql"""
        update institutions set name = ${newName} 
        where id = ${institutionId} 
          and user_id = ${session.userId}
        returning id
    """.query[Long].unique
