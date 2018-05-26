package pl.mrz.client

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import akka.util.Timeout
import pl.mrz._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class ClientActor extends Actor {
  private var remoteActor: ActorRef = _
  private implicit val timeout: Timeout = 5.seconds
  val log = Logging(context.system, this)


  implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher

  override def preStart(): Unit = {
    context
      .actorSelection("akka.tcp://book_system@127.0.0.1:3552/user/front_actor")
      .resolveOne()
      .onComplete {
        case Success(actor) =>
          context watch actor
          remoteActor = actor
        case Failure(t) => throw t
      }
  }

  override def receive: Receive = {
    case m: SearchRequest => remoteActor ! m
    case m: OrderRequest => remoteActor ! m
    case m: StreamRequest => remoteActor ! m
    case SearchReply(message) => println(message)
    case OrderReply(message) => println(message)
    case StreamReply(line) => print(line)
  }
}
