package controllers

import javax.inject._

import models.{UserForm, UserRepository}
import play.api._
import play.api.mvc.{Action, _}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, userRepository: UserRepository,
                               userForm: UserForm)
  extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def signup(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup()).withNewSession
  }

  def login(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login()).withNewSession
    //Redirect(routes.LoginController.login())
  }

  def resetpassword(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.resetPassword())
  }

  def showAddAssignment: Action[AnyContent] = Action.async { implicit request =>
    val username = request.session.get("user")
    username match {
      case Some(user) =>
        Logger.info("Get Session")
        userRepository.isAdmin(user).map {
          case true => Logger.info("Assignment Display")
            Ok(views.html.AddAssignment(userForm.assignmentForm))
          case false => Redirect(routes.HomeController.login()).withNewSession
        }
      case None => Future.successful(Redirect(routes.HomeController.login()).withNewSession)
    }
  }
/*
  def welcome(): Action[AnyContent] = Action{ implicit request =>
    Ok(views.html.home.welcome())

  }*/
}
