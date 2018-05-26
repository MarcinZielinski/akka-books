package pl.mrz

sealed class BookRequest

case class SearchRequest(title: String) extends BookRequest
case class OrderRequest(title: String) extends BookRequest
case class StreamRequest(title: String) extends BookRequest

case class SearchReply(message: String)
case class OrderReply(message: String)
case class StreamReply(line: String)

