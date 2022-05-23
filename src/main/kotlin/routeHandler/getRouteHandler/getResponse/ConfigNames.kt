package routeHandler.getRouteHandler.getResponse

import database.Connector
import database.DatabaseOperations
import org.json.JSONObject
import routeHandler.ResponseHeader
import routeHandler.StatusCodes

class ConfigNames(private val databaseOperations: DatabaseOperations):GetResponse {

    private val responseHeader = ResponseHeader()

    override fun getResponse(path:String): String{
        val responseBody = getConfigResponse()
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getConfigResponse(): String {
        val configFiles = databaseOperations.getConfigNames()
        val configJsonArrayResponse = prepareJsonResponse(configFiles)
        val responseBody = "{\"configFiles\" : $configJsonArrayResponse}"
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
}