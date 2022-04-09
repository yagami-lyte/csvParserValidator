package routeHandler

import JsonMetaDataTemplate
import com.google.gson.Gson
import org.json.JSONArray
import validation.DuplicateValidation
import java.io.BufferedReader

class PostRouteHandler(
    var fieldArray: Array<JsonMetaDataTemplate> = arrayOf()
) {


    fun handlePostRequest(request: String, inputStream: BufferedReader): String {
        val path = request.split("\r\n")[0].split(" ")[1]
        if (path == "/csv") {
            return handleCsv(request, inputStream)
        }
        if (path == "/add-meta-data") {
            handleAddMetaData(inputStream)
        }
        return ""
    }

    private fun handleCsv(request: String, inputStream: BufferedReader): String {
        var response = ""
        val contentLength = getBodySize(request)
        val content = getBody(inputStream)
        val contentInJsonArray = JSONArray(content)
        val duplicates = DuplicateValidation().checkDuplicates(contentInJsonArray)
        val typeChecks = typeValidation(contentInJsonArray)
        val lengthChecks = lengthValidation(contentInJsonArray)
        response += "{"
        response += "Type Checks : $typeChecks + Length Checks :$lengthChecks + Duplicates :$duplicates"
        val httpHead = "HTTP/1.1 200 Found"
        return httpHead + """Content-Type: text/json; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + "\r\n\r\n" + response
    }

    private fun typeValidation(contentInJsonArray: JSONArray): Any {
        TODO()
    }

    private fun lengthValidation(contentInJsonArray: JSONArray): Any {
        TODO()
    }

    private fun handleAddMetaData(inputStream: BufferedReader): String {
        val body = getBody(inputStream)
        return addMetaData(body)
    }

    fun addMetaData(body: String): String {
        val gson = Gson()
        val jsonBody = gson.fromJson(body, Array<JsonMetaDataTemplate>::class.java)
        fieldArray = jsonBody
        val endOfHeader = "\r\n\r\n"
        val responseBody = "Successfully Added"
        val contentLength = responseBody.length
        val httpHead = "HTTP/1.1 200 Found"
        return httpHead + """Content-Type: text/plain; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getBody(inputStream: BufferedReader): String {
        var body = ""
        var line = inputStream.readLine()
        while (line != null) {
            body += line
            line = inputStream.readLine()
        }
        return body
    }

    private fun getBodySize(request: String): String? {
        val length = "Content-Length: (.*)".toRegex().find(request)?.groupValues?.get(1)
        return length
    }
}