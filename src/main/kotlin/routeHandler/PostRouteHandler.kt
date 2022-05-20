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
        val responseBody = prepareErrorResponse(lengthValidation,typeValidation,valueValidation,duplicates,dependencyChecks,nullChecks,prependingZeroesChecks)
//        val responseBody = getResponse(response)
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    fun prepareErrorResponse(
        lengthValidation: MutableMap<String, MutableList<Int>>,
        typeValidation: MutableMap<String, MutableList<Int>>,
        valueValidation: MutableMap<String, MutableList<Int>>,
        duplicates: MutableMap<String, MutableList<Int>>,
        dependencyChecks: MutableMap<String, MutableList<Int>>,
        nullChecks: MutableMap<String, MutableList<Int>>,
        prependingZeroesChecks: MutableMap<String, MutableList<Int>>,
    ): String {

        val errors = JSONArray()
        fieldArray.forEach {
            val mapOfErrors = mutableMapOf<String,List<Int>>()
            val fieldName = it.fieldName
            lengthValidation.forEach{ it1 ->
                if(fieldName == it1.key) {
                    mapOfErrors["Length Errors"] = it1.value
                }
            }
            typeValidation.forEach { it1 ->
                if(fieldName == it1.key) {
                    mapOfErrors["Type Errors"] = it1.value
                }
            }
            valueValidation.forEach { it1 ->
                if(fieldName == it1.key) {
                    mapOfErrors["Value Errors"] = it1.value
                }
            }

            dependencyChecks.forEach { it1 ->
                if(fieldName == it1.key) {
                    mapOfErrors["Dependency Errors"] = it1.value
                }
            }
            nullChecks.forEach { it1 ->
                if(fieldName == it1.key) {
                    mapOfErrors["Null Errors"] = it1.value
                }
            }

            prependingZeroesChecks.forEach { it1 ->
                if(fieldName == it1.key) {
                    mapOfErrors["Prepending Errors"] = it1.value
                }
            }

            val duplicateLines = mutableListOf<Int>()
            duplicates.forEach { it1 ->
                duplicateLines.add(it1.key.toInt())
                duplicateLines.add(it1.value[0])
            }
            mapOfErrors["Duplicate Errors"] = duplicateLines

            val jsonObject = JSONObject().put(
                it.fieldName,
                mapOfErrors
            )
            errors.put(jsonObject)
        }
        return "$errors"
    }

    private fun convertToRanges(listOfErrorLines: MutableList<Int>): MutableList<String> {

        var index1 = 0
        var index2 = 0
        val listOfRangeErrors = mutableListOf<String>()
        val errorListSize = listOfErrorLines.size
        while (index1 < errorListSize) {

            index2 = index1

            while (index2 + 1 < errorListSize && listOfErrorLines[index2 + 1] === listOfErrorLines[index2] + 1) {
                index2++
            }
            if (index1 == index2) {
                val singleErrorLine = listOfErrorLines[index1].toString()
                listOfRangeErrors.add(singleErrorLine)
                index1++
            } else {
                val errorLinesInRange = "${listOfErrorLines[index1]}-${listOfErrorLines[index2]}"
                listOfRangeErrors.add(errorLinesInRange)
                index1 = index2 + 1
            }
        }

        return listOfRangeErrors
    }

    private fun getResponse(
        response: JSONArray
    ): String {
        return "$response"
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