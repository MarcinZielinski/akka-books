package pl.mrz.server.actor.worker

import java.io.{File, FileWriter}
import java.nio.file.Paths

import akka.actor.Actor
import akka.util.Timeout
import pl.mrz.OrderRequest

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

class OrderWorker extends Actor {
  private implicit val timeout: Timeout = Timeout(5.second)
  private implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher
  val pw = new FileWriter(new File(Paths.get("./src/main/resources/pl/mrz/orders").toUri), true)

  override def receive: Receive = {
    case OrderRequest(title) =>
      pw.write(title + "\n")
  }

  override def postStop(): Unit = {
    pw.close()
    super.postStop()
  }

}
