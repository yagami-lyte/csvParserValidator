package routeHandler

import ResponseHeader
import java.io.File

class GetRouteHandler {

    private val responseHeader = ResponseHeader()

    fun handleGetRequest(request: String): String {
        val route = getPath(request)
        if (route == "/") {
            return getResponse("/index.html")
        }
        return getResponse("/404.html")
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
        if(path == "/index.html"){
            return StatusCodes.TWOHUNDRED
        }
        return StatusCodes.FOURHUNDREDFOUR
    }

    private fun getBodyResponse(path: String): String {
        val filePath = System.getProperty("user.dir")
        var file = File("$filePath/src/main/public/$path")
        if (!file.exists()) {
            file = File("$filePath/src/main/public/404.html")
        }
        return file.readText(Charsets.UTF_8)

    }
}