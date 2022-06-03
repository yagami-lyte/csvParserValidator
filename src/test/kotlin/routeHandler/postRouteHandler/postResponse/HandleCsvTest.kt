package routeHandler.postRouteHandler.postResponse

import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import jsonTemplate.ConfigurationTemplate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.net.Socket

internal class HandleCsvTest {


//    @Test
//    fun shouldBeAbleToGetResponseForCsvPOSTRequest() {
//
//        val request = """POST /csv HTTP/1.1
//                |Host: localhost:3002
//                |Connection: keep-alive
//                |Content-Length: 75""".trimMargin() + "\r\n\r\n"
//        val metaData =
//            """[{"configName":"","datetime":"","date":"","time":"","nullValue":"Not Allowed","fieldName":"Export","type":"AlphaNumeric","length":"","dependentOn":"","dependentValue":""},{"configName":"","datetime":"","date":"","time":"","nullValue":"Not Allowed","fieldName":"Country Name","type":"FloatingNumber","length":"","dependentOn":"","dependentValue":""}]"""
//        val jsonData = getMetaData(metaData)
//        val handleCsv = HandleCsv()
//        handleCsv.fieldArray = jsonData
//        val csvData = """[{"Export":"Y","Country Name":""},{"Export":"N","Country Name":"USA"}]"""
//        val mockSocket = createMockSocket(csvData)
//        val inputStream = getInputStream(mockSocket)
//        val response = handleCsv.postResponse(metaData)
//        val expectedResponse ="""[{"Export":{"Duplicate Errors":[]}},{"Country Name":{"Value Errors":["3"],"Duplicate Errors":[],"Dependency Errors":["2"],"Length Errors":["3"]}}]"""
//
//        val actualErrorResponse = response.split("\r\n")[2]
//
//        assertEquals(expectedResponse, actualErrorResponse)
//    }

    private fun getInputStream(mockSocket: Socket): BufferedReader {
        return BufferedReader(InputStreamReader(mockSocket.getInputStream()))
    }

    private fun createMockSocket(data: String): Socket {
        val mockSocket = mockk<Socket>()
        every { mockSocket.getOutputStream() } returns ByteArrayOutputStream()
        every { mockSocket.getInputStream() } returns ByteArrayInputStream(data.toByteArray())
        return mockSocket
    }

    private fun getMetaData(body: String): Array<ConfigurationTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
    }

}