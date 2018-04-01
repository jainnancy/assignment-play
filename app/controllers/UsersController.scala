package controllers

import javax.inject.Inject

import play.api.mvc._

class UsersController  @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  //show all users
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok("hello index!")
  }
}
