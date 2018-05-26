package pl.mrz.client

import java.io.File

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import pl.mrz.{OrderRequest, SearchRequest, StreamRequest}

import scala.io.{Source, StdIn}

object Client extends App {

  val commandMap = Map(
    "s" -> search _,
    "o" -> order _,
    "str" -> stream _
  )

  def search(title: String): Unit = requestActor.tell(SearchRequest(title), ActorRef.noSender)
  def order(title: String): Unit = requestActor.tell(OrderRequest(title), ActorRef.noSender)
  def stream(title: String): Unit = requestActor.tell(StreamRequest(title), ActorRef.noSender)

  val config = ConfigFactory.parseFile(new File(getClass.getResource("client.conf").toURI))

  val actorSystem = ActorSystem.create("client_system", config)
  val requestActor = actorSystem.actorOf(Props[ClientActor], "client_actor")

  println("Commands:\n" +
    "\tSearch for a book: s title\n" + // returns price or info about lack of book
    "\tOrder a book: o title\n" + // returns info about order confirmation
    "\tStream a book: str title") // speed: one line per second
  while(true) {
    val input = StdIn.readLine("Enter command: ")
    val (command, title) = input.splitAt(input.indexOf(' '))
    commandMap(command).apply(title.trim)
  }
}
