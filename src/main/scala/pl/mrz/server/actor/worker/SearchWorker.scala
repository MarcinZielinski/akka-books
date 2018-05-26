package pl.mrz.server.actor.worker

import java.io.File

import akka.actor.Actor
import pl.mrz.{BookFound, BookNotFound}
import pl.mrz.server.Main
import pl.mrz.server.actor.DbSearchRequest

import scala.io.Source

class SearchWorker extends Actor {

  private val dbName = "book-db"


  override def receive: Receive = {
    case DbSearchRequest(dbNumber, title) =>
      Source
        .fromFile(new File(Main.getClass.getResource(s"../book-db$dbNumber").toURI))
        .getLines
        .find(s => s.split(":")(0) == title) match {
        case Some(book) => sender ! BookFound(book.split(":")(1).toInt)
        case None => sender ! BookNotFound
      }
  }
}
