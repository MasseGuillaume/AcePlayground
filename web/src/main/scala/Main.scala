
import akka.http.scaladsl._
import server.Directives._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.Await

object Main {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("server")
    import system.dispatcher
    implicit val materializer = ActorMaterializer()

    val route = {
      path("assets" / Remaining) { path ⇒
        getFromResource(path)
      } ~
      pathSingleSlash {
        getFromResource("index.html")
      }
    }

    Await.result(Http().bindAndHandle(route, "localhost", 8080), 20.seconds)

    ()
  }
}