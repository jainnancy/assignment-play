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

class UserForm {

  val userForm = Form(mapping(
    "firstname" -> text.verifying("first name required",_.nonEmpty),
    "middlename" -> text,
    "lastname" -> text.verifying("first name required",_.nonEmpty),
    "username"-> text.verifying("first name required",_.nonEmpty),
    "password" -> text.verifying("first name required",_.nonEmpty),
    "verifyPassword"-> text.verifying("first name required",_.nonEmpty),
    "mobile"-> text.verifying("first name required",_.nonEmpty),
    "gender"-> text.verifying("first name required",_.nonEmpty),
    "age"-> number,
    "hobbies"-> text,
    "accountType"-> text.verifying("first name required",_.nonEmpty)
  )(User.apply)(User.unapply))

}

class UserService @Inject()(cache: AsyncCacheApi) {
  def store(user: User): Future[Done] = {
    cache.set(user.username,user)
  }

  def getUser(username: String): Future[Option[User]] = {
    cache.get[User](username)
  }
}
