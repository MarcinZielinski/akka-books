package pl.mrz.server.actor

import java.io.File

import akka.actor.{Actor, ActorSelection}
import akka.pattern.ask
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout
import pl.mrz.server.Main
import pl.mrz._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

class StreamActor extends Actor {
  private implicit val timeout: Timeout = Timeout(5.second)
  private implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher
  val searchActor: ActorSelection = context.actorSelection("/user/front_actor/search_actor")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  override def receive: Receive = {
    case StreamRequest(title) =>
      val s = sender
      for (f1 <- searchActor ? SearchRequest(title))
        yield {
          f1 match {
            case BookNotFound => s ! BookNotFound
            case BookFound(_) =>
              Source
                .fromIterator(
                  scala.io.Source
                    .fromFile(new File(Main.getClass.getResource(s"../books/$title").toURI))
                    .getLines
                )
                .map(line => StreamReply(line))
                .throttle(1, 1.second)
                .runWith(Sink.actorRef(s, StreamReply("The End.")))
          }
        }
  }
}
