package pl.mrz.server

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.FromConfig
import pl.mrz.server.actor.{SearchActor, StreamActor}
import pl.mrz.{OrderRequest, SearchRequest, StreamRequest}

class FrontActor extends Actor {

  private val searchRouter: ActorRef = context.actorOf(FromConfig.props(Props[SearchActor]), "search_router")

  private val orderRouter: ActorRef = context.actorOf(FromConfig.props(Props[SearchActor]), "order_router")

  private val streamActor: ActorRef = context.actorOf(Props[StreamActor], "stream_actor")

  override def receive: Receive = {
    case r: SearchRequest => searchRouter.tell(r, sender)
    case r: OrderRequest => orderRouter.tell(r, sender)
    case r: StreamRequest => streamActor.tell(r, sender)
    case t => println(t)
  }
}