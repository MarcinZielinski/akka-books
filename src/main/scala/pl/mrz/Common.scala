package pl.mrz

case class SearchRequest(title: String)
case class SearchReply(message: String)
case class OrderRequest(title: String)
case class OrderReply(message: String)
case class StreamRequest(title: String)
case class StreamReply(line: String)

