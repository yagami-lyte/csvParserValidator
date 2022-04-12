package routeHandler

import JsonMetaDataTemplate
import ResponseHeader
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import validation.*
import java.io.BufferedReader

class PostRouteHandler {

    var fieldArray: Array<JsonMetaDataTemplate> = arrayOf()
    private val dependencyValidation = DependencyValidation()
    private val lengthValidation = LengthValidation()
    private val valueValidation = ValueValidation()
    private val typeValidation = TypeValidation()

    private val responseHeader: ResponseHeader = ResponseHeader()
    private val pageNotFoundResponse = PageNotFoundResponse()

    fun handlePostRequest(request: String, inputStream: BufferedReader): String {
        return when (getPath(request)) {
            "/csv" -> handleCsv(request, inputStream)
            "/add-meta-data" -> handleAddingCsvMetaData(request, inputStream)
            else -> pageNotFoundResponse.handleUnknownRequest()
        }
    }

    private fun getPath(request: String): String {
        return request.split("\r\n")[0].split(" ")[1].substringBefore("?")
    }

    private fun handleCsv(request: String, inputStream: BufferedReader): String {
        val bodySize = getContentLength(request)
        val body = getBody(bodySize, inputStream)
        val jsonBody = JSONArray(body)
        val lengthValidation = lengthValidation.validateLength(jsonBody, fieldArray)
        val typeValidation = typeValidation.typeCheck(jsonBody,fieldArray)
        val valueValidation = valueValidation.validationCheck(jsonBody ,fieldArray)
        val duplicates = DuplicateValidation().checkDuplicates(jsonBody)
        val dependencyChecks = dependencyValidation.checkDependency(jsonBody, fieldArray)
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
        responseBody += "}"
        print(responseBody)
        val contentLength = responseBody.length
        val endOfHeader = "\r\n\r\n"
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/json; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }




    private fun handleAddingCsvMetaData(request: String, inputStream: BufferedReader): String {
        val bodySize = getContentLength(request)
        val body = getBody(bodySize, inputStream)
        return addCsvMetaData(body)
    }

    private fun addCsvMetaData(body: String): String {
        val jsonBody = getMetaData(body)
        print(jsonBody)
        fieldArray = jsonBody
        val endOfHeader = "\r\n\r\n"
        val responseBody = "Successfully Added"
        val contentLength = responseBody.length
        return responseHeader.getResponseHead(StatusCodes.TWOHUNDRED) + """Content-Type: text/plain; charset=utf-8
    |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getBody(bodySize: Int, inputStream: BufferedReader): String {
        val buffer = CharArray(bodySize)
        inputStream.read(buffer)
        return String(buffer)
    }

    fun getMetaData(body: String): Array<JsonMetaDataTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<JsonMetaDataTemplate>::class.java)
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