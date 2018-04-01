package controllers

import javax.inject.Inject

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class LoginController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  def resetpassword() = Action { implicit request: Request[AnyContent] =>
    //Ok(views.html.resetPassword())
    Redirect(routes.LoginController.resetpassword())
  }

  def userhome() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.userHome())
  }

  def adminhome() = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.LoginController.adminhome())
  }

  def userLogin() = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.LoginController.userhome())
  }

}
