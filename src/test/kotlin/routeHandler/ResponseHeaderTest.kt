package routeHandler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ResponseHeaderTest {

    private val responseHeader = ResponseHeader()

    @Test
    fun shouldBeAbleToGetResponseHeader() {
        val statusCode = StatusCodes.TWOHUNDRED
        val expectedResponseHeader = "HTTP/1.1 200 Ok\n"

        val actualResponseHeader = responseHeader.getResponseHead(statusCode)
        println(actualResponseHeader)

        assertEquals(expectedResponseHeader , actualResponseHeader)
    }
}