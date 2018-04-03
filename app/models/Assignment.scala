package models

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.MessagesApi
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Assignment(id: Int,title: String,description: String)

trait AssignmentRepository extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  class AssignmentTable(tag: Tag) extends Table[Assignment](tag, "Assignment") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title: Rep[String] = column[String]("title")

    def description: Rep[String] = column[String]("description")
    //scalastyle:off
    def * : ProvenShape[Assignment] = (id, title, description) <> (Assignment.tupled, Assignment.unapply)
    //scalastyle:on
  }

  val assignmentQuery: TableQuery[AssignmentTable] = TableQuery[AssignmentTable]
}

class AssignmentServices @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,val messagesApi: MessagesApi)
  extends AssignmentRepository {

  implicit val messages: MessagesApi = messagesApi

  import profile.api._

  def store(assignment: Assignment): Future[Boolean] = {
    db.run(assignmentQuery += assignment).map(_ > 0)
  }

  def  returnAll(): Future[List[Assignment]] = {
    db.run(assignmentQuery.to[List].result)
  }

  def delete(id:Int): Future[Boolean] = {
    db.run(assignmentQuery.filter(_.id === id).delete).map(_ > 0)
  }
}
