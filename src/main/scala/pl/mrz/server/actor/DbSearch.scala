package pl.mrz.server.actor

import akka.actor.ActorRef
import pl.mrz.SearchReply

case class DbSearchRequest(workId: Int, dbNumber: Int, title: String, sender: ActorRef)
case class DbSearchResult(workId: Int, searchReply: SearchReply, sender: ActorRef)