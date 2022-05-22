package routeHandler.postRouteHandler.postResponse

import java.io.BufferedReader

interface PostResponse {
    fun postResponse(request: String, inputStream: BufferedReader): String
}