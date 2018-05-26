package pl.mrz.client

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import pl.mrz._

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

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
    case m: SearchRequest => remoteActor ? m onComplete {
      case Success(t) => println(t.asInstanceOf[SearchReply].message)
      case Failure(t) => throw t
    }
    case m: OrderRequest => Await.result(remoteActor ? m, timeout.duration)
    case m: StreamRequest => Await.result(remoteActor ? m, timeout.duration)
    case StreamReply(line) => print(line)
  }
}
