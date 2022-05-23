package routeHandler.postRouteHandler.postResponse

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import routeHandler.postRouteHandler.postResponse.ErrorResponse

class PageNotFoundResponseTest {

    @Test
    fun shouldReturn400BadRequest() {
        val pageNotFoundResponse = ErrorResponse()
        val expectedResponse = "HTTP/1.1 400 Bad Request" + "\r\n\r\n"

        val actualResponse = pageNotFoundResponse.handleUnknownRequest()

        assertEquals(expectedResponse, actualResponse)
    }
}