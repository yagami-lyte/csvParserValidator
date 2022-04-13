import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler
import java.net.Socket

class ServerIntegrationTest {

    private lateinit var server: Server
    @Test
    fun shouldReturn200ResponseForGetRequest() {
        val port = 3012
        startServerInThread(port)
        val clientSocket = Socket("localhost", port)
        val outputStream = clientSocket.getOutputStream()
        val inputStream = clientSocket.getInputStream()
        val request = """GET / HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        outputStream.write(request.toByteArray())
        val expectedResponseCode = "200"
        val response = String(inputStream.readAllBytes())

        val actualResponseCode = extractResponseCode(response)
        clientSocket.close()

        assertEquals(expectedResponseCode, actualResponseCode)
    }

    @Test
    fun shouldReturnPageNotFoundResponse() {
        val port = 3012
        startServerInThread(port)
        val clientSocket = Socket("localhost", port)
        val outputStream = clientSocket.getOutputStream()
        val inputStream = clientSocket.getInputStream()
        val request = """GET /123 HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        outputStream.write(request.toByteArray())
        val expectedResponseCode = "404"
        val response = String(inputStream.readAllBytes())

        val actualResponseCode = extractResponseCode(response)
        clientSocket.close()

        assertEquals(expectedResponseCode, actualResponseCode)
    }

    private fun extractResponseCode(response: String): String {
        return response.split(" ")[1]
    }

    private fun startServerInThread(port: Int) {
        Thread {
            server = Server(port)
            server.startServer()
        }.start()
    }
}