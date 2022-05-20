package routeHandler.getRouteHandler.getResponse

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class HomePageTest {

    private val homePage = HomePage()

    @Test
    fun shouldBeAbleToReturnResponseForHtmlGetRequest() {
        val htmlPath = "/index.html"
        val expectedContentType = "Content-Type: text/html"
        val response = homePage.getResponse(htmlPath)

        val actualContentType = response.split("\n")[1].split(";")[0]

        assertEquals(expectedContentType , actualContentType)
    }

    @Test
    fun shouldBeAbleToReturnResponseForJavascriptGetRequest() {
        val htmlPath = "/main.js"
        val expectedContentType = "Content-Type: text/javascript"
        val response = homePage.getResponse(htmlPath)

        val actualContentType = response.split("\n")[1].split(";")[0]

        assertEquals(expectedContentType , actualContentType)
    }

    @Test
    fun shouldBeAbleToReturnResponseForCssGetRequest() {
        val htmlPath = "/main.css"
        val expectedContentType = "Content-Type: text/css"
        val response = homePage.getResponse(htmlPath)

        val actualContentType = response.split("\n")[1].split(";")[0]

        assertEquals(expectedContentType , actualContentType)
    }
}