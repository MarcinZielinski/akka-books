package pl.mrz.server.actor

import akka.actor.Actor
import pl.mrz.{SearchReply, SearchRequest}

class SearchActor extends Actor {
  override def receive: Receive = {
    case SearchRequest(title) => sender ! SearchReply("Znaleziono: " + title)
  }
}
