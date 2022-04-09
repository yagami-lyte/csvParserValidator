import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.Socket

class ServerTest {

    @Test
    fun shouldBeAbleToGetResponseIfPathExist() {
        val server = Server()
        val request = """GET / HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        val expected = "1774"

        val response = server.handleGetRequest(request)
        val actual = "Content-Length: (.*)".toRegex().find(response)?.groupValues?.get(1)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldBeAbleToGetResponseIfPathNotExist() {
        val server = Server()
        val request = """GET /123 HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        val expected = "164"

        val response = server.handleGetRequest(request)
        println(response)
        val actual = "Content-Length: (.*)".toRegex().find(response)?.groupValues?.get(1)

        assertEquals(expected, actual)
    }
}