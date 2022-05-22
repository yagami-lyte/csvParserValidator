package routeHandler.postRouteHandler.postResponse

import database.Connector
import database.DatabaseOperations
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject
import routeHandler.ResponseHeader
import routeHandler.StatusCodes
import java.io.BufferedReader

class SendConfigurations(private val databaseOperations: DatabaseOperations) : PostResponse {

    private val responseHeader = ResponseHeader()

    override fun postResponse(request: String, inputStream: BufferedReader): String {
        val bodySize = getContentLength(request)
        val body = getBody(bodySize, inputStream)
        val regex = Regex("[^A-Za-z0-9]")
        val configData = regex.replace(body, "")
        return getResponseForConfig(configData)
    }

    private fun getContentLength(request: String): Int {
        request.split("\n").forEach { headerString ->
            val keyValue = headerString.split(":", limit = 2)
            if (keyValue[0].contains("Content-Length")) {
                return keyValue[1].trim().toInt()
            }
        }
        return 0
    }

    private fun getBody(bodySize: Int, inputStream: BufferedReader): String {
        val buffer = CharArray(bodySize)
        inputStream.read(buffer)
        return String(buffer)
    }

    fun getResponseForConfig(body: String): String {
        val responseBody = getConfigResponse(body)
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getConfigResponse(body: String): String {
        println("body $body")
        val configName = body.split("configName")[1]
        println("configName $configName")
        val configDataTemplate = databaseOperations.readConfiguration(configName)
        val configJsonArrayResponse = prepareJsonResponse(configDataTemplate)
        var responseBody = "{"
        responseBody += "\"Type\" : $configJsonArrayResponse"
        responseBody += "}"
        return responseBody
    }

    private fun prepareJsonResponse(configDataTemplate: Array<ConfigurationTemplate>): JSONArray {
        val jsonArrayOfConfigData = JSONArray()
        configDataTemplate.forEach {
            val jsonObject = JSONObject()
            //jsonObject.put("configName", it.configName)
            jsonObject.put("type", it.type)
            jsonObject.put("length", it.length)
            jsonObject.put("dateTime", it.datetime)
            jsonObject.put("date", it.date)
            jsonObject.put("time", it.time)
            jsonObject.put("values", it.values)
            jsonObject.put("dependentOn", it.dependentOn)
            jsonObject.put("dependentValue", it.dependentValue)
            jsonObject.put("nullValue", it.nullValue)

            val fieldJsonObject = JSONObject().put(it.fieldName, jsonObject)
            jsonArrayOfConfigData.put(fieldJsonObject)
        }
        println("jsonArrayOfConfigData $jsonArrayOfConfigData")
        return jsonArrayOfConfigData
    }
}