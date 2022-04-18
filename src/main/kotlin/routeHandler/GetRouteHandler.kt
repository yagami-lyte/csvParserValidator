package routeHandler

import java.io.File

class GetRouteHandler {

    private val responseHeader = ResponseHeader()
    private val contentType = mapOf(
        "/index.html" to "text/html",
        "/main.css" to "text/css",
        "/main.js" to "text/javascript"
    )

    fun handleGetRequest(request: String): String {
        return when (getPath(request)) {
            "/" -> getResponse("/index.html")
            "/main.js" -> getResponse("/main.js")
            "/main.css" -> getResponse("/main.css")
            else -> getResponse("/404.html")
        }
    }

    private fun getPath(request: String): String {
        return request.split("\r\n")[0].split(" ")[1]
    }

    private fun getResponse(path: String): String {
        val body = getBodyResponse(path)
        val contentLength = body.length
        val statusCode = getStatusCode(path)
        return responseHeader.getResponseHead(statusCode) + """Content-Type: ${contentType[path]}; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + "\r\n\r\n" + body
    }

    private fun getStatusCode(path: String): StatusCodes {
        if (path == "/index.html" || path == "/main.js" || path == "/main.css") {
            return StatusCodes.TWOHUNDRED
        }
        return StatusCodes.FOURHUNDREDFOUR
    }

    private fun getBodyResponse(path: String): String {
        val filePath = System.getProperty("user.dir")
        val file = File("$filePath/src/main/public$path")
        return file.readText(Charsets.UTF_8)
    }
}