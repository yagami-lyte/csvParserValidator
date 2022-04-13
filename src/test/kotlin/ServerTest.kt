import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.Socket
import io.mockk.mockk
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader

class ServerTest {

    private val server = Server(3002)

    @Test
    fun shouldBeAbleToGetResponseForGetRequest() {
        val request = """GET / HTTP/1.1 
                |Host: localhost:3002""".trimMargin() + "\r\n\r\n"
        val csvData = """[{"Export":"Y","Country Name":""},{"Export":"N","Country Name":"USA"}]"""
        val mockSocket = createMockSocket(csvData)
        val inputStream = BufferedReader(InputStreamReader(mockSocket.getInputStream()))
        val expectedResponseCode = "200"
        val response = server.handleRequest(request , inputStream)

        val actualResponseCode = extractResponseCode(response)

        assertEquals(actualResponseCode , expectedResponseCode)
    }


    private fun createMockSocket(csvData: String): Socket {
        val mockSocket = mockk<Socket>()
        every { mockSocket.getOutputStream() } returns ByteArrayOutputStream()
        every { mockSocket.getInputStream() } returns ByteArrayInputStream(csvData.toByteArray())
        return mockSocket
    }

    private fun extractResponseCode(response: String): String {
        return response.split("\n")[0].split(" ")[1]
    }
}