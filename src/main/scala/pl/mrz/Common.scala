package pl.mrz

sealed trait BookRequest

case class SearchRequest(title: String) extends BookRequest
case class OrderRequest(title: String) extends BookRequest
case class StreamRequest(title: String) extends BookRequest

case class OrderReply(message: String)
case class StreamReply(line: String)

sealed trait SearchReply

case class BookFound(price: Int) extends SearchReply
case object BookNotFound extends SearchReply