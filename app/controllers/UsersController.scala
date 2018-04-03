package controllers

import javax.inject.Inject

import models.{UpdateProfile, UserForm, UserRepository}
import play.api.mvc.{Action, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UsersController @Inject()(cc: ControllerComponents, userRepository: UserRepository,
                                userForm: UserForm) extends AbstractController(cc) {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok("hello index!")
  }

  def updatePassword(): Action[AnyContent] = Action.async { implicit request =>
    userForm.updatePasswordForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(Redirect(routes.HomeController.resetpassword())
          .flashing("Error" -> "Invalid Username or password format!"))
      },
      data => {
        userRepository.getUser(data.username).flatMap {
          case Some(user) =>
            userRepository.updatePassword(data.username, data.password).map {

              case true =>
                Redirect(routes.HomeController.login())
                  .flashing("Success" -> "Password Changed")
                  .withNewSession

              case false =>
                Redirect(routes.HomeController.resetpassword())
                  .flashing("Error" -> "Try Again!")
            }
          case None =>
            Future.successful(Redirect(routes.HomeController.resetpassword())
              .flashing("Error" -> "Invalid username!"))
        }
      }
    )
  }

  def showProfile: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

    val currentUser = request.session.get("user")
    currentUser match {
      case Some(username) => userRepository.getUser(username).flatMap {
        case None => Future.successful(Redirect(routes.UsersController.showProfile())
          .flashing("Error" -> "No such User."))
        case Some(user) =>
          val userProfile: UpdateProfile = UpdateProfile(user.firstname, user.middlename, user.lastname,
            user.mobile, user.gender, user.age, user.hobbies)
          val abc = Future.successful(Ok(views.html.profileUpdate(userForm.updateProfileForm.fill(userProfile))))
          println(abc)
          Future.successful(Ok(views.html.profileUpdate(userForm.updateProfileForm.fill(userProfile))))
      }
      case None => Future.successful(Redirect(routes.HomeController.login())
        .flashing("Error" -> "Please Login Again")
        .withNewSession)
    }
  }

  def updateProfile(): Action[AnyContent] = Action.async { implicit request =>
    val username = request.session.get("user")
    username match {
      case Some(user) =>
        userForm.updateProfileForm.bindFromRequest.fold(
          formWithErrors => {
            Future.successful(Redirect(routes.UsersController.showProfile())
              .flashing("Error" -> "Invalid Details"))
          },
          userProfile => {
            userRepository.updateDetails(userProfile, user).flatMap {
              case true => Future.successful(Redirect(routes.UsersController.showProfile())
                .flashing("Success" -> "Details updated successfully"))
              case false => Future.successful(Redirect(routes.UsersController.showProfile())
                .flashing("Error" -> "Error while updating details"))
            }
          })
      case None => Future.successful(Redirect(routes.HomeController.login())
        .flashing("Error" -> "Please Login Again")
        .withNewSession)
    }
  }

}
