package routeHandler

import com.google.gson.Gson
import database.Connector
import database.DatabaseOperations
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject
import validation.*
import java.io.BufferedReader

class PostRouteHandler(var fieldArray: Array<ConfigurationTemplate> = arrayOf()) {

    private val dependencyValidation = DependencyValidation()
    private val lengthValidation = LengthValidation()
    private val valueValidation = ValueValidation()
    private val typeValidation = TypeValidation()
    private val duplicateValidation = DuplicateValidation()
    private val nullValidation = NullValidation()
    private val responseHeader: ResponseHeader = ResponseHeader()
    private val pageNotFoundResponse = PageNotFoundResponse()
    private val prependingZeroesValidation = PrependingZeroesValidation()

    fun handlePostRequest(request: String, inputStream: BufferedReader): String {
        return when (getPath(request)) {
            "/csv" -> handleCsv(request, inputStream)
            "/add-meta-data" -> handleAddingCsvMetaData(request, inputStream)
            "/get-config-response" -> handleSendConfigResponse(request, inputStream)
            else -> pageNotFoundResponse.handleUnknownRequest()
        }
    }

    private fun getPath(request: String): String {
        return request.split("\r\n")[0].split(" ")[1].substringBefore("?")
    }

    private fun handleSendConfigResponse(request :String, inputStream : BufferedReader) : String{
        val bodySize = getContentLength(request)
        val body = getBody(bodySize, inputStream)
        return getResponseForConfig(body)
    }

    fun getResponseForConfig(body: String): String {
        val responseBody = getConfigResponse(body)
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getConfigResponse(body :String) :String{
        val databaseOperations = DatabaseOperations(Connector())
        val configName = body.split(":")[1].replace("\"", "").replace("}]" , "")
        println("configName $configName")
        //val jsonObject = JSONObject()
        var responseBody = ""
        val configDataTemplate = databaseOperations.readConfiguration(configName)
        val configJsonArrayResponse = prepareJsonResponse(configDataTemplate)
        responseBody = "{"
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
            jsonObject.put("date" , it.date)
            jsonObject.put("time" , it.time)
            jsonObject.put("values", it.values)
            jsonObject.put("dependentOn", it.dependentOn)
            jsonObject.put("dependentValue", it.dependentValue)
            jsonObject.put("nullValue", it.nullValue)

            val fieldJsonObject = JSONObject().put(it.fieldName,jsonObject)
            jsonArrayOfConfigData.put(fieldJsonObject)
        }
        println("jsonArrayOfConfigData $jsonArrayOfConfigData")
        return jsonArrayOfConfigData
    }

    private fun handleCsv(request: String, inputStream: BufferedReader): String {
        val bodySize = getContentLength(request)
        val body = getBody(bodySize, inputStream)
        return getResponseForCSV(body)
    }

    fun getResponseForCSV(body: String): String {
        val jsonBody = JSONArray(body)
        val lengthValidation = lengthValidation.validate(jsonBody, fieldArray)
        val typeValidation = typeValidation.validate(jsonBody, fieldArray)
        val valueValidation = valueValidation.validate(jsonBody, fieldArray)
        val duplicates = duplicateValidation.validate(jsonBody , fieldArray)
        val dependencyChecks = dependencyValidation.validate(jsonBody, fieldArray)
        val nullChecks = nullValidation.validate(jsonBody,fieldArray)
        val prependingZeroesChecks = prependingZeroesValidation.validate(jsonBody,fieldArray)
        val responseBody = getResponse(duplicates, lengthValidation, typeValidation, valueValidation, dependencyChecks, nullChecks,prependingZeroesChecks)
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getResponse (
        duplicates: JSONArray,
        lengthValidation: JSONArray,
        typeValidation: JSONArray,
        valueValidation: JSONArray,
        dependencyChecks: Any,
        nullChecks:JSONArray,
        prependingZeroesValidation: JSONArray
    ): String {
        var responseBody = "{"
        responseBody += "\"Duplicates\" : $duplicates"
        responseBody += ","
        responseBody += "\"Length\" : $lengthValidation"
        responseBody += ","
        responseBody += "\"Type\" : $typeValidation"
        responseBody += ","
        responseBody += "\"Value\" : $valueValidation"
        responseBody += ","
        responseBody += "\"Dependency\" : $dependencyChecks"
        responseBody += ","
        responseBody += "\"Null\" : $nullChecks"
        responseBody += ","
        responseBody += "\"PrependingZeroes\" : $prependingZeroesValidation"
        responseBody += "}"
        return responseBody
    }


    private fun handleAddingCsvMetaData(request: String, inputStream: BufferedReader): String {
        val bodySize = getContentLength(request)
        val body = getBody(bodySize, inputStream)
        return getResponseForMetaData(body)
    }

    private fun getResponseForMetaData(body: String): String {
        val jsonBody = getMetaData(body)
        fieldArray = jsonBody
        val configName = fieldArray.first().configName
        val databaseOperations = DatabaseOperations(Connector())
        println(configName)
        if(configName != null && configName != "") {
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

    private fun getBody(bodySize: Int, inputStream: BufferedReader): String {
        val buffer = CharArray(bodySize)
        inputStream.read(buffer)
        return String(buffer)
    }

    fun getMetaData(body: String): Array<ConfigurationTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
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
    
}