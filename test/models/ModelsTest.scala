package models

import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.WithApplicationLoader

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.reflect.ClassTag

class ModelsTest[T: ClassTag] extends WithApplicationLoader{

/*  lazy val appFake: Application =  {
    new GuiceApplicationBuilder().build()
  }*/
  lazy val app2doo = Application.instanceCache
  lazy val repository: T = app2doo(app)

  def result[R](response: Future[R]): R =
    Await.result(response, Duration.Inf)

}
