package pl.mrz.server

import akka.actor.ActorRef

case class ConnectedClient(actor: ActorRef)
