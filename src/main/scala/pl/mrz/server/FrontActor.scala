package pl.mrz.server

import akka.actor.Actor
import pl.mrz.{SearchReply, SearchRequest}

class FrontActor extends Actor {
  override def receive: Receive = {
    case SearchRequest(title) => sender ! SearchReply(s"Znaleziono! Tytuł: $title")
  }
}