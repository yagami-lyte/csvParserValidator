package routeHandler.getRouteHandler.getResponse

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ErrorPageTest {

    private val errorPage = ErrorPage()

    @Test
    fun shouldReturn404PageErrorForInvalidHTMLGetRequest() {
        val errorPagePath = "/abc.html"
        val expectedResponse = "HTTP/1.1 404 NOT FOUND"
        val response = errorPage.getResponse(errorPagePath)

        val actualResponse = response.split('\n')[0]

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun shouldReturn404PageErrorForInvalidJavascriptFileGetRequest() {
        val errorPagePath = "/xyz.js"
        val expectedResponse = "HTTP/1.1 404 NOT FOUND"
        val response = errorPage.getResponse(errorPagePath)

        val actualResponse = response.split('\n')[0]

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun shouldReturn404PageErrorForInvalidCSSFileGetRequest() {
        val errorPagePath = "/pqr.css"
        val expectedResponse = "HTTP/1.1 404 NOT FOUND"
        val response = errorPage.getResponse(errorPagePath)

        val actualResponse = response.split('\n')[0]

        assertEquals(expectedResponse, actualResponse)
    }
}