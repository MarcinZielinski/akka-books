package pl.mrz.server.actor

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.FromConfig
import pl.mrz.SearchRequest
import pl.mrz.server.actor.worker.SearchWorker

class SearchActor extends Actor {
  private val searchRouter: ActorRef = context.actorOf(FromConfig.props(Props[SearchWorker]), "search_router")

  override def receive: Receive = {
    case SearchRequest(title) =>
      searchRouter.!(DbSearchRequest(1, title))(sender)
      searchRouter.!(DbSearchRequest(2, title))(sender)
  }

  def searchDatabase(): Unit = {

  }
}
