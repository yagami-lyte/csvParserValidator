package routeHandler

import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import jsonTemplate.JsonMetaDataTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.*
import java.net.Socket

internal class PostRouteHandlerTest {

    @Test
    fun shouldBeAbleToGetResponseForCsvPOSTRequest() {

        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"INDIA"},{"Export":"N","Country Name":"USA"}]"""
        val expectedErrorResponse =
            """{"Duplicates" : [],"Length" : [{"1":"Incorrect length of Country Name. Please change its length to 4"},{"2":"Incorrect length of Country Name. Please change its length to 4"}],"Type" : [],"Value" : [{"1":"Incorrect Value of Country Name. Please select value from [Export,Country Name, Y,, N,USA, ]"},{"2":"Incorrect Value of Country Name. Please select value from [Export,Country Name, Y,, N,USA, ]"}],"Dependency" : []}"""
        val response = postRouteHandler.getResponseForCSV(csvData)

        val actualErrorResponse = response.split("\r\n\r\n")[1]
        println(actualErrorResponse)

        assertEquals(expectedErrorResponse, actualErrorResponse)
    }

    @Test
    fun shouldBeAbleToGetResponseForConfigPOSTRequest() {
        val request = """GET /add-meta-data HTTP/1.1 
                |Host: localhost:3002
                |Connection: keep-alive
                |Content-Length: 266""".trimMargin() + "\r\n\r\n"
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"INDIA"},{"Export":"N","Country Name":"USA"}]"""
        val mockSocket = createMockSocket(metaData)
        val inputStream = BufferedReader(InputStreamReader(mockSocket.getInputStream()))
        val outputStream = BufferedWriter(OutputStreamWriter(mockSocket.getOutputStream()))
        outputStream.write(csvData)
        val response = postRouteHandler.handlePostRequest(request , inputStream)
        val expectedResponse = "Successfully Added Configuration File"

        val actualErrorResponse = response.split("\r\n\r\n")[1]

        assertEquals(expectedResponse, actualErrorResponse)
    }

    private fun createMockSocket(csvData: String): Socket {
        val mockSocket = mockk<Socket>()
        every { mockSocket.getOutputStream() } returns ByteArrayOutputStream()
        every { mockSocket.getInputStream() } returns ByteArrayInputStream(csvData.toByteArray())
        return mockSocket
    }
}

private fun getMetaData(data: String): Array<JsonMetaDataTemplate> {
    val gson = Gson()
    return gson.fromJson(data, Array<JsonMetaDataTemplate>::class.java)
}


