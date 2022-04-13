package routeHandler

import java.io.File

class GetRouteHandler {

    private val responseHeader = ResponseHeader()

    fun handleGetRequest(request: String): String {
        return when (getPath(request)) {
            "/" -> getResponse("/index.html")
            "/main.js" -> getResponse("/main.js")
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
        return responseHeader.getResponseHead(statusCode) + """Content-Type: text/html; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + "\r\n\r\n" + body
    }

    private fun getStatusCode(path: String): StatusCodes {
        if (path == "/index.html" || path == "/main.js") {
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