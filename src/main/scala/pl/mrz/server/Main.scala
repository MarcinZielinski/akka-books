package pl.mrz.server

import java.io.{File, PrintWriter}
import java.nio.file.Paths

import akka.NotUsed
import akka.actor.{ActorSystem, Props}
import akka.stream._
import akka.stream.scaladsl._
import com.typesafe.config.ConfigFactory
import pl.mrz.client.Client.getClass

import scala.concurrent._
import scala.concurrent.duration._
import scala.io.StdIn

object Main extends App {
  val config = ConfigFactory.parseFile(new File(getClass.getResource("remote.conf").toURI))


  implicit val system: ActorSystem = ActorSystem("book_system", config)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val actor = system.actorOf(Props[FrontActor], "front_actor")

  //val source: Source[Int, NotUsed] = Source(1 to 100)
  //val done: Future[Done] = source.runForeach(i ⇒ println(i))(materializer)


//  val factorials = source.scan(BigInt(1))((acc, next) ⇒ acc * next)

//  val result: Future[IOResult] =
//    factorials
//      .map(num ⇒ ByteString(s"$num\n"))
//      .runWith(FileIO.toPath(Paths.get("factorials.txt")))

//  val result = factorials
//    .zipWith(Source(0 to 100))((num, idx) ⇒ s"$idx! = $num")
//    .throttle(1, 1.second)
//    .runForeach(println)
//  def lineSink(filename: String): Sink[String, Future[IOResult]] =
//    Flow[String]
//      .map(s ⇒ ByteString(s + "\n"))
//      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
//
//  val result = factorials.map(_.toString).runWith(lineSink("factorial2.txt"))

  //implicit val ec: ExecutionContextExecutor = system.dispatcher
  //result.onComplete(_ ⇒ system.terminate())

  if(StdIn.readLine("Type q to exit...") == "q") {
    system.terminate()
  }
}
