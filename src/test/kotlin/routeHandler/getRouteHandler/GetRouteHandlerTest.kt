package routeHandler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import routeHandler.getRouteHandler.GetRouteHandler

class GetRouteHandlerTest {

    @Disabled
    fun shouldBeAbleToGetResponseIfPathExist() {
        val getRouteHandler = GetRouteHandler()
        val request = """GET / HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        val expected = "545"
        val response = getRouteHandler.handleGetRequest(request)

        val actual = "Content-Length: (.*)".toRegex().find(response)?.groupValues?.get(1)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldBeAbleToGetResponseIfPathNotExist() {
        val getRouteHandler = GetRouteHandler()
        val request = """GET /123 HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        val expected = "164"
        val response = getRouteHandler.handleGetRequest(request)
        println(response)

        val actual = "Content-Length: (.*)".toRegex().find(response)?.groupValues?.get(1)

        assertEquals(expected, actual)
    }
}