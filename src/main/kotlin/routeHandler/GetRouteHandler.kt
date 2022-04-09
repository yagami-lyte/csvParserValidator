package routeHandler

import java.io.File

class GetRouteHandler {

    fun handleGetRequest(request: String): String {
        val path = getPath(request)
        if (path == "/") {
            return getResponse("/index.html")
        }
        return getResponse("/404.html")
    }

    private fun getPath(request: String): String {
        val path = request.split("\r\n")[0].split(" ")[1]
        return path
    }

    private fun getResponse(path: String): String {
        val body = getBodyResponse(path)
        val httpHead = "HTTP/1.1 200 Found"
        val contentLength = body.length
        return httpHead + """Content-Type: text/html; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + "\r\n\r\n" + body
    }

    private fun getBodyResponse(path: String): String {
        val filePath = System.getProperty("user.dir")
        var file = File("$filePath/src/main/public/$path")
        if (file.exists() === false) {
            var file = File("$filePath/src/main/public/404.html")
        }
        return file.readText(Charsets.UTF_8)

    }
}