package routeHandler.postRouteHandler

import com.google.gson.Gson
import database.Connector
import database.DatabaseOperations
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject
import routeHandler.PageNotFoundResponse
import routeHandler.ResponseHeader
import routeHandler.StatusCodes
import routeHandler.postRouteHandler.postResponse.HandleCSVMetaData
import routeHandler.postRouteHandler.postResponse.HandleCsv
import routeHandler.postRouteHandler.postResponse.SendConfigurations
import validation.*
import java.io.BufferedReader

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class PostRouteHandler(var fieldArray: Array<ConfigurationTemplate> = arrayOf()) {

    private val handleCsv = HandleCsv()
    private val handleAddingCsvMetaData = HandleCSVMetaData()
    private val sendConfigurations = SendConfigurations(DatabaseOperations(Connector()))
    private val pageNotFoundResponse = PageNotFoundResponse()

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