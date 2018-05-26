package pl.mrz.server.actor

import akka.actor.Actor
import pl.mrz.OrderRequest

class OrderActor extends Actor {
  override def receive: Receive = {
    case OrderRequest(title) =>
  }
}
