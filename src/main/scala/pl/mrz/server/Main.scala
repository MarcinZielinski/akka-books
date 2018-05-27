package pl.mrz.server

import java.io.File

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

object Main extends App {
  val config = ConfigFactory.parseFile(new File(getClass.getResource("remote.conf").toURI))
  implicit val system: ActorSystem = ActorSystem("book_system", config)

  val actor = system.actorOf(Props[FrontActor], "front_actor")

  if (StdIn.readLine("Type q to exit...") == "q") {
    system.terminate()
  }
}
