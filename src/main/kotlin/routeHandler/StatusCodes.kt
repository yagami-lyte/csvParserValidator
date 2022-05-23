package routeHandler

enum class StatusCodes(val statusCode: Int, val message: String) {
    TWOHUNDRED(200, "Ok"),
    FOURHUNDREDFOUR(404, "NOT FOUND"),

}