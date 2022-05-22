package routeHandler.postRouteHandler.postResponse

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject
import routeHandler.ResponseHeader
import routeHandler.StatusCodes
import validation.*
import java.io.BufferedReader
import java.io.File

class HandleCsv(var fieldArray: Array<ConfigurationTemplate> = arrayOf()): PostResponse{

    private val dependencyValidation = DependencyValidation()
    private val lengthValidation = LengthValidation()
    private val valueValidation = ValueValidation()
    private val typeValidation = TypeValidation()
    private val duplicateValidation = DuplicateValidation()
    private val nullValidation = NullValidation()
    private val responseHeader: ResponseHeader = ResponseHeader()
    private val prependingZeroesValidation = PrependingZeroesValidation()

    override fun postResponse(request: String, inputStream: BufferedReader): String {
        val bodySize = getContentLength(request)
        val body = getBody(bodySize, inputStream)
        return getResponseForCSV(body)
    }

    private fun getConfigResponse(): String {
        val filePath = System.getProperty("user.dir")
        val file = File("$filePath/src/main/kotlin/resources/config.json")
        return file.readText(Charsets.UTF_8)
    }

    fun getMetaData(body: String): Array<ConfigurationTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
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
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun prepareErrorResponse(
        lengthValidation: MutableMap<String, MutableList<String>>,
        typeValidation: MutableMap<String, MutableList<String>>,
        valueValidation: MutableMap<String, MutableList<String>>,
        duplicates: MutableMap<String, MutableList<String>>,
        dependencyChecks: MutableMap<String, MutableList<String>>,
        nullChecks: MutableMap<String, MutableList<String>>,
        prependingZeroesChecks: MutableMap<String, MutableList<String>>,
    ): String {

        val errors = JSONArray()
        fieldArray.forEach {
            val mapOfErrors = mutableMapOf<String,List<String>>()
            val fieldName = it.fieldName
            lengthValidation.forEach{ it1 ->
                if(fieldName == it1.key) {
                    val listOfRangeErrors = convertToRanges(it1.value)
                    mapOfErrors["Length Errors"] = listOfRangeErrors
                }
            }
            typeValidation.forEach { it1 ->
                if(fieldName == it1.key) {
                    val listOfRangeErrors = convertToRanges(it1.value)
                    mapOfErrors["Type Errors"] = listOfRangeErrors
                }
            }
            valueValidation.forEach { it1 ->
                if(fieldName == it1.key) {
                    val listOfRangeErrors = convertToRanges(it1.value)
                    mapOfErrors["Value Errors"] = listOfRangeErrors
                }
            }

            dependencyChecks.forEach { it1 ->
                if(fieldName == it1.key) {
                    val listOfRangeErrors = convertToRanges(it1.value)
                    mapOfErrors["Dependency Errors"] = listOfRangeErrors
                }
            }
            nullChecks.forEach { it1 ->
                if(fieldName == it1.key) {
                    val listOfRangeErrors = convertToRanges(it1.value)
                    mapOfErrors["Null Errors"] = listOfRangeErrors
                }
            }

            prependingZeroesChecks.forEach { it1 ->
                if(fieldName == it1.key) {
                    val listOfRangeErrors = convertToRanges(it1.value)
                    mapOfErrors["Prepending Errors"] = listOfRangeErrors
                }
            }

            val duplicateLines = mutableListOf<String>()
            duplicates.forEach { it1 ->
                duplicateLines.add(it1.key.toInt().toString())
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

    private fun convertToRanges(listOfErrorLines: MutableList<String>): MutableList<String> {

        var index1 = 0
        var index2: Int
        val listOfRangeErrors = mutableListOf<String>()
        val lineErrorsList = listOfErrorLines.map(String::toInt)
        val errorListSize = listOfErrorLines.size
        while (index1 < errorListSize) {

            index2 = index1

            while (index2 + 1 < errorListSize && lineErrorsList[index2 + 1] === lineErrorsList[index2] + 1) {
                index2++
            }
            if (index1 == index2) {
                val singleErrorLine = lineErrorsList[index1]
                listOfRangeErrors.add(singleErrorLine.toString())
                index1++
            } else {
                val errorLinesInRange = "${lineErrorsList[index1]}-${lineErrorsList[index2]}"
                listOfRangeErrors.add(errorLinesInRange)
                index1 = index2 + 1
            }
        }
        return listOfRangeErrors
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

}