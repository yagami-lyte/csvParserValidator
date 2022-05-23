package routeHandler.postRouteHandler

import database.Connector
import database.DatabaseOperations
import jsonTemplate.ConfigurationTemplate
import routeHandler.postRouteHandler.postResponse.ErrorResponse
import routeHandler.postRouteHandler.postResponse.HandleCSVMetaData
import routeHandler.postRouteHandler.postResponse.HandleCsv
import routeHandler.postRouteHandler.postResponse.SendConfigurations
import java.io.BufferedReader

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class PostRouteHandler(var fieldArray: Array<ConfigurationTemplate> = arrayOf()) {

    private val handleCsv = HandleCsv()
    private val handleAddingCsvMetaData = HandleCSVMetaData()
    private val sendConfigurations = SendConfigurations(DatabaseOperations(Connector()))
    private val pageNotFoundResponse = ErrorResponse()

    fun handlePostRequest(request: String, inputStream: BufferedReader): String {
        return when (getPath(request)) {
            "/csv" -> handleCsv.postResponse(request, inputStream)
            "/add-meta-data" -> handleAddingCsvMetaData.postResponse(request, inputStream)
            "/get-config-response" -> sendConfigurations.postResponse(request, inputStream)
            else -> pageNotFoundResponse.handleUnknownRequest()
        }
    }

    private fun getPath(request: String): String {
        return request.split("\r\n")[0].split(" ")[1].substringBefore("?")
    }
}