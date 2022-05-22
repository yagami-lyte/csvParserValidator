package routeHandler.postRouteHandler.postResponse

import com.google.gson.Gson
import database.Connector
import database.DatabaseOperations
import jsonTemplate.ConfigurationTemplate
import routeHandler.ResponseHeader
import routeHandler.StatusCodes
import java.io.BufferedReader
import java.io.File

class HandleCSVMetaData(var fieldArray: Array<ConfigurationTemplate> = arrayOf()) : PostResponse {

    private val responseHeader = ResponseHeader()

    override fun postResponse(request: String, inputStream: BufferedReader): String {
        val bodySize = getContentLength(request)
        val body = getBody(bodySize, inputStream)
        return getResponseForMetaData(body)
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

    private fun getResponseForMetaData(body: String): String {
        storeConfigDataInAFile(body)
        val jsonBody = getMetaData(body)
        fieldArray = jsonBody
        val configName = fieldArray.first().configName
        val databaseOperations = DatabaseOperations(Connector())
        println(configName)
        if (configName != null && configName != "") {
            databaseOperations.saveNewConfigurationInDatabase(configName)
            fieldArray.forEach {
                databaseOperations.writeConfiguration(configName, it)
            }
        }
        val endOfHeader = "\r\n\r\n"
        val responseBody = "Successfully Added Configuration File"
        val contentLength = responseBody.length
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/plain; charset=utf-8
    |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    fun getMetaData(body: String): Array<ConfigurationTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
    }

    private fun storeConfigDataInAFile(fieldArray: String) {
        val pathToProjectDirectory = System.getProperty("user.dir")
        File("$pathToProjectDirectory/src/main/kotlin/resources/config.json").writeText(fieldArray)
    }

}