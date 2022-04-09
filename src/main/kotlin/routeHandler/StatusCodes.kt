package routeHandler

enum class StatusCodes(val statusCodes: Int,val message: String) {
    TWOHUNDRED(200,"Ok"),
    FOURHUNDREDFOUR(404,"NOT FOUND"),
    FOURHUNDREDONE(401,"Unauthorized"),
    FOURHUNDRED(400,"Bad Request")
}