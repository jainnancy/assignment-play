package controllers

import javax.inject.Inject

import models.{UserForm, UserRepository}
import play.api.Logger
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class LoginController @Inject()(userform: UserForm, userRepository: UserRepository,
                                cc: ControllerComponents) extends AbstractController(cc) {

  def userhome(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.userHome())
  }

  def adminhome(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.adminHome())
    //Redirect(routes.LoginController.adminhome())
  }

  def userLogin(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.LoginController.userhome())
  }

  def loginCheck: Action[AnyContent] = Action.async { implicit request =>
    userform.loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(Redirect(routes.HomeController.login()).flashing("Error" -> "Fill Form Correctly"))
      },
      data => {
        userRepository.getUser(data.username).flatMap {
          case Some(userName) =>
            userRepository.checkPasswords(data.username, data.password).flatMap {
              case true => userRepository.isAdmin(data.username).flatMap {
                case true => Future.successful(Redirect(routes.LoginController.adminhome())
                  .withSession("user" -> data.username))
                case false => Future.successful(Redirect(routes.LoginController.userhome())
                  .withSession("user" -> data.username))
              }
              case false => Future.successful(Redirect(routes.HomeController.login()).flashing("Error" -> "Wrong Username or Password"))
            }
          case None => Future.successful(Redirect(routes.HomeController.login()).flashing("Error" -> "No user by this username"))
        }
      }
    )
  }
}
