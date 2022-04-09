package routeHandler

class PageNotFoundResponse {

    fun handleUnknownRequest(): String {
        val httpHead = "HTTP/1.1 400 Bad Request"
        val lineSeparator = "\r\n\r\n"
        return httpHead + lineSeparator
    }
}