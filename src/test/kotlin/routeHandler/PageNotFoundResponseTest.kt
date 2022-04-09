package routeHandler

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PageNotFoundResponseTest {

    @Test
    fun shouldReturn400BadRequest() {
        val pageNotFoundResponse = PageNotFoundResponse()
        val expectedResponse = "HTTP/1.1 400 Bad Request"  + "\r\n\r\n"

        val actualResponse = pageNotFoundResponse.handleUnknownRequest()

        assertEquals(expectedResponse,actualResponse)
    }
}