package routeHandler

import routeHandler.getRouteHandler.GetRouteHandler
import routeHandler.postRouteHandler.PostRouteHandler
import java.io.BufferedReader

class RouteHandler {

    private val getRouteHandler = GetRouteHandler()
    private val postRouteHandler = PostRouteHandler()
    private val pageNotFoundResponse = PageNotFoundResponse()

    fun handleRequest(request: String, inputStream: BufferedReader,methodType:String): String {
        return when (methodType) {
            "GET" -> getRouteHandler.handleGetRequest(request)
            "POST" -> postRouteHandler.handlePostRequest(request, inputStream)
            else -> pageNotFoundResponse.handleUnknownRequest()
        }
    }
}