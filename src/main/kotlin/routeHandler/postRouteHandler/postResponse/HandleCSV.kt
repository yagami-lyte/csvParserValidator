package routeHandler.postRouteHandler.postResponse

import Extractor
import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject
import routeHandler.ResponseHeader
import validation.*
import java.io.File


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class HandleCsv(var fieldArray: Array<ConfigurationTemplate> = arrayOf()) : PostResponse {
    private val dependencyValidation = DependencyValidation()
    private val lengthValidation = LengthValidation()
    private val valueValidation = ValueValidation()
    private val typeValidation = TypeValidation()
    private val duplicateValidation = DuplicateValidation()
    private val nullValidation = NullValidation()
    private val responseHeader: ResponseHeader = ResponseHeader()
    private val prependingZeroesValidation = PrependingZeroesValidation()
    private val extractor = Extractor()

    override fun postResponse(configData : String): String {
        return getResponseForCSV(configData)
    }

    private fun getResponseForCSV(body: String): String {
        val configBody = getConfigResponse()
        fieldArray = getMetaData(configBody)
        val jsonBody = JSONArray(body)
        val lengthChecks = lengthValidation.validate(jsonBody, fieldArray)
        val typeChecks = typeValidation.validate(jsonBody, fieldArray)
        val valueChecks = valueValidation.validate(jsonBody, fieldArray)
        val duplicateChecks = duplicateValidation.validate(jsonBody, fieldArray)
        val dependencyChecks = dependencyValidation.validate(jsonBody, fieldArray)
        val nullChecks = nullValidation.validate(jsonBody, fieldArray)
        val prependingZeroesChecks = prependingZeroesValidation.validate(jsonBody, fieldArray)
        val responseBody = prepareErrorResponse(
            lengthChecks,
            typeChecks,
            valueChecks,
            duplicateChecks,
            dependencyChecks,
            nullChecks,
            prependingZeroesChecks
        )
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
//        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
//                |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
        return responseBody
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
            val mapOfErrors = mutableMapOf<String, List<String>>()
            val fieldName = it.fieldName
            lengthValidation.map { it1 -> appendErrorsForRespectiveField(mapOfErrors, it1, fieldName, "Length Errors") }
            typeValidation.map { it1 -> appendErrorsForRespectiveField(mapOfErrors, it1, fieldName, "Type Errors") }
            valueValidation.map { it1 -> appendErrorsForRespectiveField(mapOfErrors, it1, fieldName, "Value Errors") }
            dependencyChecks.map { it1 -> appendErrorsForRespectiveField(mapOfErrors, it1, fieldName,"Dependency Errors") }
            nullChecks.map { it1 -> appendErrorsForRespectiveField(mapOfErrors, it1, fieldName, "Null Errors") }
            prependingZeroesChecks.map { it1 -> appendErrorsForRespectiveField(mapOfErrors, it1, fieldName, "PrependingZero Errors") }
            val duplicateLines = getDuplicateErrors(duplicates)
            mapOfErrors["Duplicate Errors"] = duplicateLines
            val jsonObject = getJsonObjectForRespectiveField(it, mapOfErrors)
            errors.put(jsonObject)
        }
        return "$errors"
    }

    private fun getJsonObjectForRespectiveField(
        it: ConfigurationTemplate,
        mapOfErrors: MutableMap<String, List<String>>,
    ): JSONObject? {
        return JSONObject().put(
            it.fieldName,
            mapOfErrors
        )
    }

    private fun getDuplicateErrors(duplicates: MutableMap<String, MutableList<String>>): MutableList<String> {
        val duplicateLines = mutableListOf<String>()
        duplicates.map { it1 ->
            duplicateLines.add(it1.key.toInt().toString())
            duplicateLines.add(it1.value[0])
        }
        return duplicateLines
    }

    private fun appendErrorsForRespectiveField(
        mapOfErrors: MutableMap<String, List<String>>,
        it1: Map.Entry<String, MutableList<String>>,
        fieldName: String,
        errorHeading: String,
    ) {
        if (fieldName == it1.key) {
            mapOfErrors[errorHeading] = convertToRanges(it1.value)
        }
    }

    private fun convertToRanges(listOfErrorLines: MutableList<String>): MutableList<String> {

        var index1 = 0
        var index2: Int
        val listOfRangeErrors = mutableListOf<String>()
        val lineErrorsList = listOfErrorLines.map(String::toInt)
        val errorListSize = listOfErrorLines.size
        while (index1 < errorListSize) {
            index2 = index1
            index2 = incrementConsecutiveErrors(index2, errorListSize, lineErrorsList)
            index1 = appendErrorInList(index1, index2, lineErrorsList, listOfRangeErrors)
        }
        return listOfRangeErrors
    }

    private fun appendErrorInList(
        index1: Int,
        index2: Int,
        lineErrorsList: List<Int>,
        listOfRangeErrors: MutableList<String>,
    ): Int {
        return when (index2) {
            index1 -> getSingleLineError(lineErrorsList, index1, listOfRangeErrors)
            else -> getConsecutiveErrorsInRanges(lineErrorsList, index1, index2, listOfRangeErrors)
        }
    }

    private fun getSingleLineError(
        lineErrorsList: List<Int>,
        index11: Int,
        listOfRangeErrors: MutableList<String>,
    ): Int {
        var index111 = index11
        val singleErrorLine = lineErrorsList[index111]
        listOfRangeErrors.add(singleErrorLine.toString())
        index111++
        return index111
    }

    private fun getConsecutiveErrorsInRanges(
        lineErrorsList: List<Int>,
        index11: Int,
        index2: Int,
        listOfRangeErrors: MutableList<String>,
    ): Int {
        var index111 = index11
        val errorLinesInRange = "${lineErrorsList[index111]}-${lineErrorsList[index2]}"
        listOfRangeErrors.add(errorLinesInRange)
        index111 = index2 + 1
        return index111
    }

    private fun incrementConsecutiveErrors(
        index2: Int,
        errorListSize: Int,
        lineErrorsList: List<Int>,
    ): Int {
        var index21 = index2
        while (index21 + 1 < errorListSize && lineErrorsList[index21 + 1] === lineErrorsList[index21] + 1) {
            index21++
        }
        return index21
    }

    private fun getMetaData(body: String): Array<ConfigurationTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
    }

    private fun getConfigResponse(): String {
        val filePath = System.getProperty("user.dir")
        val file = File("$filePath/src/main/kotlin/resources/config.json")
        return file.readText(Charsets.UTF_8)
    }
}
