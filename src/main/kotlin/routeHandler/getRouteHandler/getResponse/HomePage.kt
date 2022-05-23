package routeHandler.getRouteHandler.getResponse

import Extractor
import routeHandler.ResponseHeader
import routeHandler.StatusCodes
import java.io.File

class HomePage : GetResponse {

    private val responseHeader = ResponseHeader()
    private val extractor = Extractor()
    private val contentType = mapOf(
        "/index.html" to "text/html",
        "/main.css" to "text/css",
        "/main.js" to "text/javascript"
    )

    override fun getResponse(path: String): String {
        val body = extractor.extractFileContent(path)
        val contentLength = body.length
        val statusCode = extractor.extractStatusCode(path)
        return responseHeader.getResponseHead(statusCode) + """Content-Type: ${contentType[path]}; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + "\r\n\r\n" + body
    }

}