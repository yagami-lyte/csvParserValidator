package routeHandler.postRouteHandler.postResponse

import com.google.gson.Gson
import database.DatabaseOperations
import database.TestConnector
import io.mockk.every
import io.mockk.mockk
import jsonTemplate.ConfigurationTemplate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import routeHandler.postRouteHandler.postResponse.SendConfigurations
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.net.Socket

internal class SendConfigurationsTest{

    private val sendConfigurations = SendConfigurations(DatabaseOperations(TestConnector()))

    @Test
    fun shouldBeAbleToSendConfigurationsFromTheDatabase() {
        val request = """POST /get-config-response HTTP/1.1 
                |Host: localhost:3002
                |Connection: keep-alive
                |Content-Length: 266""".trimMargin() + "\r\n\r\n"
        val metaData =
            """[{"configName":"configuration","datetime":"MMMM dd, yy","date":"","time":"","nullValue":"Not Allowed","fieldName":"operation","type":"Date Time","length":"","dependentOn":"","dependentValue":""},{"configName":"","datetime":"","date":"","time":"","nullValue":"Not Allowed","fieldName":"requestedAt","type":"Alphabets","length":"","dependentOn":"","dependentValue":""}]"""
        val configName = """[{"configName":"configuration"}]"""
        val mockSocket = createMockSocket(configName)
        val inputStream = getInputStream(mockSocket)
        println(inputStream.toString())
        val databaseOperations = DatabaseOperations(TestConnector())
        val jsonData = createJsonTemplateWithDependencyFields(metaData)
        databaseOperations.saveNewConfigurationInDatabase("configuration")
        jsonData.forEach {
            databaseOperations.writeConfiguration("configuration",it)
        }

        val expectedConfigurationsReceived = """{"Type" : [{"operation":{"dateTime":"MMMM dd, yy","date":"","dependentValue":"","dependentOn":"","values":[],"length":"","time":"","type":"Date Time","nullValue":"Not Allowed"}},{"requestedAt":{"dateTime":"","date":"","dependentValue":"","dependentOn":"","values":[],"length":"","time":"","type":"Alphabets","nullValue":"Not Allowed"}}]}"""
        val actualConfigurationsReceived = sendConfigurations.postResponse(request , inputStream).split("\r\n")[2]

        assertEquals(expectedConfigurationsReceived , actualConfigurationsReceived)
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


    private fun createJsonTemplateWithDependencyFields(metaData:String): Array<ConfigurationTemplate> {
        return Gson().fromJson(metaData, Array<ConfigurationTemplate>::class.java)
    }
}