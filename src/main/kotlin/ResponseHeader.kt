import routeHandler.StatusCodes

class ResponseHeader {

    fun getResponseHead(statusCodeMessage: StatusCodes): String {
        val head = statusCodeMessage.message
        val statusCode = statusCodeMessage.statusCode
        return "HTTP/1.1 $statusCode $head\n"
    }
}