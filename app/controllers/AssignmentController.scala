package controllers

import javax.inject.Inject

import models.{Assignment, AssignmentServices, UserForm, UserRepository}
import play.api.i18n.I18nSupport

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc.{Action, _}

import scala.concurrent.Future

class AssignmentController @Inject()(assingmentServices: AssignmentServices, userForm: UserForm,
                                     userRepository: UserRepository, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  def addAssignment(): Action[AnyContent] = Action.async { implicit request =>

    val username = request.session.get("user")

    username match {
      case Some(user) =>
        userRepository.isAdmin(user).flatMap {

          case true =>
            userForm.assignmentForm.bindFromRequest.fold(
              formWithErrors => {
                Future.successful(Redirect(routes.HomeController.showAddAssignment()).flashing("Error" -> "Fill Form Correctly"))
              },
              assignmentData => {
                val assignment = Assignment(1, assignmentData.title, assignmentData.description)
                assingmentServices.store(assignment).map {
                  case true => Redirect(routes.HomeController.showAddAssignment()).flashing("Success" -> "Assignment SuccessFully Added")
                  case false => Redirect(routes.HomeController.showAddAssignment()).flashing("Error" -> "Error While Storing")
                }
              })

          case false => Future.successful(Redirect(routes.HomeController.login()).flashing("Error" -> "No Session").withNewSession)

        }
      case None => Future.successful(Redirect(routes.HomeController.login()).flashing("Error" -> "No Session").withNewSession)
    }
  }

  def viewAssignmentUser: Action[AnyContent] =Action.async{ implicit request=>
    val username = request.session.get("user")
    username match {
      case Some(user) => userRepository.isAdmin(user).flatMap {
        case true => Future.successful(Redirect(routes.HomeController.login())
          .flashing("Error" -> "Try to Login as User"))

        case false => assingmentServices.returnAll.map {
          case assignments: List[Assignment] =>
            Ok(views.html.ViewAssignmentUser(assignments))
        }
      }
      case None => Future.successful(Redirect(routes.HomeController.login())
        .flashing("Error" -> "Session out").withNewSession)
    }
  }

  def viewAssignmentAdmin: Action[AnyContent] =Action.async{ implicit request =>
    val username = request.session.get("user")
    username match {
      case Some(user) => userRepository.isAdmin(user).flatMap {
        case true =>
          assingmentServices.returnAll().map {
            case assignments: List[Assignment] =>
              Ok(views.html.ViewAssignmentAdmin(assignments))
          }

        case false => Future.successful(Redirect(routes.HomeController.login())
          .flashing("Error" -> "Try to Login as Admin").withNewSession)

      }
      case None => Future.successful(Redirect(routes.HomeController.login())
        .flashing("Error" -> "No Session").withNewSession)
    }
  }

  def deleteAssignment(id: Int): Action[AnyContent] = Action.async{ implicit request  =>
    val username = request.session.get("user")
    username match {
      case Some(user) => userRepository.isAdmin(user).flatMap {
        case true =>
          assingmentServices.delete(id).map {
            case true => Redirect(routes.AssignmentController.viewAssignmentAdmin())
              .flashing("Success"->"SuccessFully Deleted")
            case false => Redirect(routes.AssignmentController.viewAssignmentAdmin())
              .flashing("Error" -> "Error While Deleting")
          }

        case false => Future.successful(Redirect(routes.HomeController.login())
          .flashing("Error" -> "Try to Login as Admin").withNewSession)

      }
      case None => Future.successful(Redirect(routes.HomeController.login())
        .flashing("Error" -> "No Session").withNewSession)
    }
  }

}
