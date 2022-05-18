package routeHandler

import database.Connector
import database.DatabaseOperations
import org.json.JSONObject
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
            "/get-config-files" -> sendConfigFileNames()
            else -> getResponse("/404.html")
        }
    }

    private fun sendConfigFileNames(): String{
        val responseBody = getConfigResponse()
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getConfigResponse() :String{
        val databaseOperations = DatabaseOperations(Connector())
        var responseBody = ""
        val configFiles = databaseOperations.getConfigNames()
        val configJsonArrayResponse = prepareJsonResponse(configFiles)
        responseBody = "{"
            responseBody += "\"configFiles\" : $configJsonArrayResponse"
        responseBody += "}"
        println("responsecofig : $responseBody")
        return responseBody
    }


    private fun prepareJsonResponse(configDataTemplate: List<String>?): JSONObject {
        var countOfConfig = 1
        val jsonObject = JSONObject()
        configDataTemplate?.forEach {
            jsonObject.put("$countOfConfig", it)
            countOfConfig += 1
        }

        return jsonObject
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