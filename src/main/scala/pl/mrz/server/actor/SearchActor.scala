package pl.mrz.server.actor

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.routing.FromConfig
import akka.util.Timeout
import pl.mrz.{BookFound, BookNotFound, SearchReply, SearchRequest}
import pl.mrz.server.actor.worker.SearchWorker

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

class SearchActor extends Actor {
  private val searchRouter: ActorRef = context.actorOf(FromConfig.props(Props[SearchWorker]), "search_router")
  private implicit val timeout: Timeout = Timeout(5.second)
  private implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher

  override def receive: Receive = {
    case SearchRequest(title) =>
      val s = sender
      for (f1 <- searchRouter ? DbSearchRequest(1, title);
           f2 <- searchRouter ? DbSearchRequest(2, title)
      ) yield {
        (f1, f2) match {
          case (BookNotFound, BookNotFound) => s ! BookNotFound
          case (l, r) => l match {
            case _: BookFound => s ! l
            case _            => s ! r
          }
        }
      }
  }

  def searchDatabase(): Unit = {

  }
}
