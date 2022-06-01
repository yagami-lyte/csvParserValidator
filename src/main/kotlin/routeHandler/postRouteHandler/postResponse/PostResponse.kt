package routeHandler.postRouteHandler.postResponse

import java.io.BufferedReader

interface PostResponse {
    fun postResponse(configData : String): String
}