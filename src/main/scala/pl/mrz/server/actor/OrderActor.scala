package pl.mrz.server.actor

import akka.actor.{Actor, ActorSelection}
import akka.pattern.ask
import akka.util.Timeout
import pl.mrz._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

class OrderActor extends Actor {
  private implicit val timeout: Timeout = Timeout(5.second)
  private implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher
  val searchActor: ActorSelection = context.actorSelection("/user/front_actor/search_actor")
  val orderWorker: ActorSelection = context.actorSelection("/user/front_actor/order_worker")

  override def receive: Receive = {
    case o: OrderRequest =>
      println(searchActor)
      val s = sender
      for (f1 <- searchActor ? SearchRequest(o.title))
        yield {
          f1 match {
            case BookNotFound => s ! BookNotFound
            case BookFound(_) =>
              orderWorker ! o
              s ! OrderSuccess
          }
        }
  }
}
