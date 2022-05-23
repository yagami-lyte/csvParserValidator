package routeHandler.postRouteHandler

import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import jsonTemplate.ConfigurationTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import routeHandler.postRouteHandler.postResponse.HandleCsv
import java.io.*
import java.net.Socket

internal class PostRouteHandlerTest {

    private val postRouteHandler = PostRouteHandler()
    private val handleCsv = HandleCsv()

    @Test
    fun shouldBeAbleToGetResponseForCsvPOSTRequest() {

        val request = """POST /csv HTTP/1.1 
                |Host: localhost:3002
                |Connection: keep-alive
                |Content-Length: 75""".trimMargin() + "\r\n\r\n"
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val jsonData = getMetaData(metaData)
        handleCsv.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"INDIA"},{"Export":"N","Country Name":"USA"}]"""
        val mockSocket = createMockSocket(csvData)
        val inputStream = getInputStream(mockSocket)
        val response = postRouteHandler.handlePostRequest(request , inputStream)
        val expectedResponse = """[{"Export":{"Duplicate Errors":[]}},{"Country Name":{"Length Errors":["2-3"],"Value Errors":["2-3"],"Duplicate Errors":[]}}]"""

        val actualErrorResponse = response.split("\r\n\r\n")[1]

        assertEquals(expectedResponse, actualErrorResponse)
    }

    @Test
    fun shouldBeAbleToGetResponseForConfigPOSTRequest() {
        val request = """POST /add-meta-data HTTP/1.1 
                |Host: localhost:3002
                |Connection: keep-alive
                |Content-Length: 266""".trimMargin() + "\r\n\r\n"
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val mockSocket = createMockSocket(metaData)
        val inputStream = getInputStream(mockSocket)
        val response = postRouteHandler.handlePostRequest(request , inputStream)
        val expectedResponse = "Successfully Added Configuration File"

        val actualErrorResponse = response.split("\r\n\r\n")[1]

        assertEquals(expectedResponse, actualErrorResponse)
    }

    private fun getInputStream(mockSocket: Socket): BufferedReader {
        return BufferedReader(InputStreamReader(mockSocket.getInputStream()))
    }

    private fun createMockSocket(data: String): Socket {
        val mockSocket = mockk<Socket>()
        every { mockSocket.getOutputStream() } returns ByteArrayOutputStream()
        every { mockSocket.getInputStream() } returns ByteArrayInputStream(data.toByteArray())
        return mockSocket
    }
}

private fun getMetaData(data: String): Array<ConfigurationTemplate> {
    val gson = Gson()
    return gson.fromJson(data, Array<ConfigurationTemplate>::class.java)
}


