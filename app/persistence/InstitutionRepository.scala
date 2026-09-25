package persistence

import models.{Institution, Time}

import javax.inject.{Inject, Singleton}

import org.typelevel.doobie.ConnectionIO
import org.typelevel.doobie.implicits.*

@Singleton
class InstitutionRepository @Inject():
  
  def list(userId: Long): ConnectionIO[List[Institution]] =
    sql"""
        select * 
        from institutions 
        where user_id = $userId
        order by name
    """.query[Institution].to[List]

  def findById(id: Long, userId: Long): ConnectionIO[Option[Institution]] =
    sql"""
        select * 
        from institutions 
        where id = $id
          and user_id = $userId
    """.query[Institution].option
    
  def findByName(name: String, userId: Long): ConnectionIO[Option[Institution]] =
    sql"""
        select * 
        from institutions 
        where name = $name
          and user_id = $userId
    """.query[Institution].option
    
  def insert(name: String, userId: Long): ConnectionIO[Long] =
    sql"""
        insert into institutions (user_id, name) 
        values ($userId, $name)
        returning id
    """.query[Long].unique

  def archive(institutionId: Long, userId: Long): ConnectionIO[Time] =
    sql"""
        update institutions set archived_at = ${Time.now} 
        where id = $institutionId
          and user_id = $userId
        returning archived_at
    """.query[Time].unique

  def unarchive(institutionId: Long, userId: Long): ConnectionIO[Long] =
    sql"""
        update institutions set archived_at = null 
        where id = $institutionId 
        and user_id = $userId
        returning id 
    """.query[Long].unique

  def rename(institutionId: Long, userId: Long)(newName: String): ConnectionIO[Long] =
    sql"""
        update institutions set name = $newName
        where id = $institutionId 
          and user_id = $userId
        returning id
    """.query[Long].unique
