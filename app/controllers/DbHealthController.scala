package controllers

import play.api.*
import play.api.mvc.*

import persistence.HealthRepository

import javax.inject.*

@Singleton
class DbHealthController @Inject() (val controllerComponents: ControllerComponents, repo: HealthRepository) extends BaseController:
  def health() = Action { implicit request: Request[?] =>
    Ok(repo.select1.toString)
  }
