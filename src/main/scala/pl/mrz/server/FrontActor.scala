package pl.mrz.server

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.FromConfig
import pl.mrz.server.actor.worker.OrderWorker
import pl.mrz.server.actor.{OrderActor, SearchActor, StreamActor}
import pl.mrz.{OrderRequest, SearchRequest, StreamRequest}

class FrontActor extends Actor {

  private val searchActor: ActorRef = context.actorOf(Props[SearchActor], "search_actor")

  private val orderRouter: ActorRef = context.actorOf(FromConfig.props(Props[OrderActor]), "order_router")

  private val streamActor: ActorRef = context.actorOf(Props[StreamActor], "stream_actor")

  private val orderWorker: ActorRef = context.actorOf(Props[OrderWorker], "order_worker")

  override def receive: Receive = {
    case r: SearchRequest => searchActor.!(r)(sender)
    case r: OrderRequest => orderRouter.!(r)(sender)
    case r: StreamRequest => streamActor.!(r)(sender)
  }
}