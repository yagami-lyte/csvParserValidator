package routeHandler.getRouteHandler.getResponse

import routeHandler.ResponseHeader
import routeHandler.StatusCodes
import java.io.File

class ErrorPage:GetResponse {

    private val responseHeader = ResponseHeader()

    override fun getResponse(path: String): String {
        val body = getBodyResponse(path)
        val contentLength = body.length
        val statusCode = StatusCodes.FOURHUNDREDFOUR
        return responseHeader.getResponseHead(statusCode) + """Content-Type: text/html; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + "\r\n\r\n" + body
    }

    private fun getBodyResponse(path: String): String {
        val filePath = System.getProperty("user.dir")
        val file = File("$filePath/src/main/public/404.html")
        return file.readText(Charsets.UTF_8)
    }
}