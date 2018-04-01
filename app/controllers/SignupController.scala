package controllers

import javax.inject.Inject

import models.{User, UserForm, UserRepository}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SignupController @Inject()(userform: UserForm, userRepository: UserRepository,
                                 cc: ControllerComponents) extends AbstractController(cc) {

  def storeData: Action[AnyContent] = Action.async { implicit request =>
    userform.userForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.signup()))
      },
      data => {
        userRepository.getUser(data.username).flatMap { optionalRecord =>

          optionalRecord.fold {
            val record = User(data.firstname, data.middlename, data.lastname, data.username, data.password,
              data.verifyPassword, data.mobile, data.gender, data.age, data.hobbies, data.accountType)
            userRepository.store(record).map { _ =>
              Ok("Stored")
            }
          } { _ =>
            Future.successful(InternalServerError("User already exist."))
          }
        }
      })
  }

  def getData(username: String): Action[AnyContent] = Action.async {
    userRepository.getUser(username).map { record =>
      record.fold {
        NotFound("User not found!")
      }{
        record =>
          Ok(s"User's full name = ${record.firstname} ${record.middlename} ${record.lastname}")
      }

    }
  }
}
