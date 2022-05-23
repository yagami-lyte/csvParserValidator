package routeHandler.getRouteHandler.getResponse

import Extractor
import routeHandler.ResponseHeader
import routeHandler.StatusCodes
import java.io.File

class ErrorPage : GetResponse {

    private val responseHeader = ResponseHeader()
    private val extractor = Extractor()

    override fun getResponse(path: String): String {
        val body = extractor.extractErrorPageFileContent()
        val contentLength = body.length
        val statusCode = StatusCodes.FOURHUNDREDFOUR
        return responseHeader.getResponseHead(statusCode) + """Content-Type: text/html; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + "\r\n\r\n" + body
    }


}