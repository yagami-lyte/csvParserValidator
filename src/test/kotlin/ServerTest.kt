import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.Socket

class ServerTest {


    @Test
    fun shouldBeAbleToGetResponse() {
        val server = Server()
        val request = """GET / HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        val expectedResponse = "GetRequest"

        val actualResponse = server.handleGetRequest(request)

        assertEquals(expectedResponse , actualResponse)
    }
}