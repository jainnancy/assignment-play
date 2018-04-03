package models

import javax.inject.Inject

import akka.Done
import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.cache.AsyncCacheApi

import scala.concurrent.Future

case class User(firstname: String,
                middlename:String,
                lastname: String,
                username: String,
                password: String,
                verifyPassword:String,
                mobile: String,
                gender: String,
                age: Int,
                hobbies: String,
                accountType: String
               )

case class LoginUser(username: String, password: String)

case class UpdatePassword(username: String, password: String, verifyPassword:String)

case class UpdateProfile(firstname: String,
                         middlename:String,
                         lastname: String,
                         mobile: String,
                         gender: String,
                         age: Int,
                         hobbies: String
                        )

case class AssignmentForm(title: String, description: String)
class UserForm {

  val userForm = Form(mapping(
    "firstname" -> text.verifying("first name required",_.nonEmpty),
    "middlename" -> text,
    "lastname" -> text.verifying("last name required",_.nonEmpty),
    "username"-> text.verifying("username required",_.nonEmpty),
    "password" -> text.verifying("password required",_.nonEmpty),
    "verifyPassword"-> text.verifying("verify password required",_.nonEmpty),
    "mobile"-> text.verifying("mobile number required",_.nonEmpty),
    "gender"-> text.verifying("gender required",_.nonEmpty),
    "age"-> number,
    "hobbies"-> text,
    "accountType"-> text.verifying("Account type required",_.nonEmpty)
  )(User.apply)(User.unapply)
  verifying("Password mismatch", field => field.password.equals(field.verifyPassword)))

  val loginForm = Form(mapping(
    "username" -> text.verifying("username required",_.nonEmpty),
    "password" -> text.verifying("password required",_.nonEmpty)
  )(LoginUser.apply)(LoginUser.unapply))

  val updatePasswordForm = Form(mapping(
    "username" -> text.verifying("username required",_.nonEmpty),
    "password" -> text.verifying("password required",_.nonEmpty),
    "verifyPassword"-> text.verifying("verify password required",_.nonEmpty)
  )(UpdatePassword.apply)(UpdatePassword.unapply)
  verifying("Password mismatch", field => field.password.equals(field.verifyPassword)))

  val updateProfileForm = Form(mapping(
    "firstname" -> text.verifying("first name required",_.nonEmpty),
    "middlename" -> text,
    "lastname" -> text.verifying("last name required",_.nonEmpty),
    "mobile"-> text.verifying("mobile number required",_.nonEmpty),
    "gender"-> text.verifying("gender required",_.nonEmpty),
    "age"-> number,
    "hobbies"-> text
  )(UpdateProfile.apply)(UpdateProfile.unapply))

  val assignmentForm: Form[AssignmentForm] = Form(mapping(
    "title" -> text.verifying("Title required",_.nonEmpty),
    "description" -> text.verifying("Descripton required",_.nonEmpty)
  )(AssignmentForm.apply)(AssignmentForm.unapply))
}

class UserService @Inject()(cache: AsyncCacheApi) {
  def store(user: User): Future[Done] = {
    cache.set(user.username,user)
  }

  def getUser(username: String): Future[Option[User]] = {
    cache.get[User](username)
  }
}
