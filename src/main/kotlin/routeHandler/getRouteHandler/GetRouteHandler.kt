package routeHandler.getRouteHandler

import database.Connector
import database.DatabaseOperations
import routeHandler.getRouteHandler.getResponse.ConfigNames
import routeHandler.getRouteHandler.getResponse.ErrorPage
import routeHandler.getRouteHandler.getResponse.HomePage

class GetRouteHandler {

    private val configNames = ConfigNames(DatabaseOperations(Connector()))
    private val errorPage = ErrorPage()
    private val homePage = HomePage()

    fun handleGetRequest(request: String): String {
        return when (getPath(request)) {
            "/" -> homePage.getResponse("/index.html")
            "/main.js" -> homePage.getResponse("/main.js")
            "/main.css" -> homePage.getResponse("/main.css")
            "/get-config-files" -> configNames.getResponse("/get-config-files")
            else -> errorPage.getResponse("/404.html")
        }
    }

    private fun getPath(request: String): String {
        return request.split("\r\n")[0].split(" ")[1]
    }
}