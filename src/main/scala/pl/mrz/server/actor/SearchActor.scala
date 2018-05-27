package pl.mrz.server.actor

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.FromConfig
import akka.util.Timeout
import pl.mrz.server.actor.worker.SearchWorker
import pl.mrz.{BookFound, BookNotFound, SearchRequest}

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

class SearchActor extends Actor {
  private val searchRouter: ActorRef = context.actorOf(FromConfig.props(Props[SearchWorker]), "search_router")
  private implicit val timeout: Timeout = Timeout(5.second)
  private implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher
  private val workMap = scala.collection.mutable.Map[Int, MapState]()
  private val workId: AtomicInteger = new AtomicInteger(0)

  override def receive: Receive = {
    case SearchRequest(title) =>
      workMap(workId.incrementAndGet) = WaitingForFirstResult
      searchRouter ! DbSearchRequest(workId.get, 1, title, sender)
      searchRouter ! DbSearchRequest(workId.get, 2, title, sender)
    case DbSearchResult(currId, searchReply, client) =>
      searchReply match {
        case m: BookFound =>
          workMap(currId) match {
            case WaitingForFirstResult | WaitingForSecondResult =>
              workMap(currId) = Found
              client ! m
            case _ =>
          }
        case BookNotFound =>
          workMap(currId) match {
            case WaitingForFirstResult => workMap(currId) = WaitingForSecondResult
            case WaitingForSecondResult => client ! BookNotFound
            case _ =>
          }
      }
  }

  sealed class MapState

  case object WaitingForFirstResult extends MapState

  case object WaitingForSecondResult extends MapState

  case object Found extends MapState

}
