package pl.mrz.server.actor.worker

import java.io.File

import akka.actor.Actor
import pl.mrz.server.Main
import pl.mrz.server.actor.{DbSearchRequest, DbSearchResult}
import pl.mrz.{BookFound, BookNotFound}

import scala.io.Source

class SearchWorker extends Actor {

  private val dbName = "book-db"


  override def receive: Receive = {
    case DbSearchRequest(workId, dbNumber, title, client) =>
      val s = sender
      try {
        Source
          .fromFile(new File(Main.getClass.getResource(s"../book-db$dbNumber").toURI))
          .getLines
          .find(s => s.split(":")(0) == title) match {
          case Some(book) => s ! DbSearchResult(workId, BookFound(book.split(":")(1).toInt), client)
          case None => s ! DbSearchResult(workId, BookNotFound, client)
        }
      } catch {
        case _: Exception => s ! DbSearchResult(workId, BookNotFound, client)
      }
  }
}
