package routeHandler.postRouteHandler.postResponse

import Extractor
import database.DatabaseOperations
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject
import routeHandler.ResponseHeader
import routeHandler.StatusCodes
import java.io.BufferedReader

class SendConfigurations(private val databaseOperations: DatabaseOperations) : PostResponse {

    private val responseHeader = ResponseHeader()
    private val extractor = Extractor()

    override fun postResponse(request: String, inputStream: BufferedReader): String {
        val bodySize = extractor.extractContentLength(request)
        val body = extractor.extractBody(bodySize, inputStream)
        return getResponseForConfig(body)
    }

    private fun getResponseForConfig(body: String): String {
        val responseBody = getConfigResponse(body)
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getConfigResponse(body: String): String {
        val configName = body.split('"')[3]
        val configDataTemplate = databaseOperations.readConfiguration(configName)
        val configJsonArrayResponse = prepareJsonResponse(configDataTemplate)
        return "{\"Type\" : $configJsonArrayResponse}"
    }

    private fun prepareJsonResponse(configDataTemplate: Array<ConfigurationTemplate>): JSONArray {
        val jsonArrayOfConfigData = JSONArray()

        configDataTemplate.map {
            val jsonObject = prepareJsonObjectForField(it)
            val fieldConfig = JSONObject().put(it.fieldName, jsonObject)
            jsonArrayOfConfigData.put(fieldConfig)
        }

        return jsonArrayOfConfigData
    }

    private fun prepareJsonObjectForField(it: ConfigurationTemplate): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", it.type)
        jsonObject.put("length", it.length)
        jsonObject.put("dateTime", it.datetime)
        jsonObject.put("date", it.date)
        jsonObject.put("time", it.time)
        jsonObject.put("values", it.values)
        jsonObject.put("dependentOn", it.dependentOn)
        jsonObject.put("dependentValue", it.dependentValue)
        jsonObject.put("nullValue", it.nullValue)
        return jsonObject
    }
}