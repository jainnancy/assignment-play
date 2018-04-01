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

    def firstname = column[String]("firstname")

    def middlename = column[String]("middlename")

    def lastname = column[String]("lastname")

    def username = column[String]("username")

    def password = column[String]("password")

    def verifyPassword = column[String]("verifyPassword")

    def mobile = column[String]("mobile")

    def gender = column[String]("gender")

    def age = column[Int]("age")

    def hobbies = column[String]("hobbies")

    def accountType = column[String]("accountType")

    def * : ProvenShape[User] =
      (firstname, middlename, lastname, username, password, verifyPassword, mobile,
        gender, age, hobbies, accountType) <> (User.tupled, User.unapply)
  }

  val userQuery = TableQuery[UserTable]
}

trait UserBaseRepository {

  def store(user: User): Future[Boolean]

  def getUser(username: String): Future[Option[User]]
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
}

class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
extends UserBaseRepository with UserBaseRepositoryImpl with UserRepositoryTable
