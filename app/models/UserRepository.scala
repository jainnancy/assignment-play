package models

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UserRepositoryTable extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "User") {

    def firstname: Rep[String] = column[String]("firstname")

    def middlename: Rep[String] = column[String]("middlename")

    def lastname: Rep[String] = column[String]("lastname")

    def username: Rep[String] = column[String]("username")

    def password: Rep[String] = column[String]("password")

    def verifyPassword: Rep[String] = column[String]("verifyPassword")

    def mobile: Rep[String] = column[String]("mobile")

    def gender: Rep[String] = column[String]("gender")

    def age: Rep[Int] = column[Int]("age")

    def hobbies: Rep[String] = column[String]("hobbies")

    def accountType: Rep[String] = column[String]("accountType")
    //scalastyle:off
    def * : ProvenShape[User] =
      (firstname, middlename, lastname, username, password, verifyPassword, mobile,
        gender, age, hobbies, accountType) <> (User.tupled, User.unapply)
    //scalastyle:on
  }

  val userQuery = TableQuery[UserTable]
}

trait UserBaseRepository {

  def store(user: User): Future[Boolean]

  def getUser(username: String): Future[Option[User]]

  def checkPasswords(username:String, password : String): Future[Boolean]

  def isAdmin(username:String): Future[Boolean]

  def updatePassword(username: String, password: String): Future[Boolean]

  def updateDetails(updateProfile: UpdateProfile, username:String): Future[Boolean]
}

trait UserBaseRepositoryImpl extends UserBaseRepository {

  self: UserRepositoryTable =>
  import profile.api._

  override def store(user: User): Future[Boolean] = {
    db.run(userQuery += user) map(_ > 0)
  }

  override def getUser(username: String): Future[Option[User]] = {
    val queryResult = userQuery.filter(_.username.toLowerCase === username.toLowerCase)
      .result.headOption
    db.run(queryResult)
  }

  override def checkPasswords(username: String, password: String): Future[Boolean] = {

    val users = db.run(userQuery.filter(_.username === username).result)
    users.map { user =>
      if (user.isEmpty) {
        false
      }
      else if (user.head.password == password) {
          true
      }
      else {
        false
      }
    }
  }

  override def isAdmin(username: String): Future[Boolean] = {
    val users: Future[Seq[User]] = db.run(userQuery.filter(_.username === username).filter(_.accountType === "Admin").result)
    users.map { user =>
      if (user.isEmpty) {
        false
      }
      else {
        true
      }
    }
  }

  override def updatePassword(username: String, password: String): Future[Boolean] = {
    db.run(userQuery.filter(_.username === username).map(_.password).update(password)).map(_ > 0)
    db.run(userQuery.filter(_.username === username).map(_.verifyPassword).update(password)).map(_ > 0)
  }

  override def updateDetails(updateProfile: UpdateProfile, username: String): Future[Boolean] = {
    db.run(userQuery.filter(_.username===username).map(user=> (user.firstname,user.middlename,
      user.lastname, user.mobile,user.age,user.gender,user.hobbies)).update(updateProfile.firstname,
      updateProfile.middlename, updateProfile.lastname, updateProfile.mobile, updateProfile.age,
      updateProfile.gender,updateProfile.hobbies)).map(_ > 0)
  }
}

class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
extends UserBaseRepository with UserBaseRepositoryImpl with UserRepositoryTable
