import routeHandler.StatusCodes
import java.io.BufferedReader
import java.io.File

class Extractor {

    fun extractFileContent(path: String): String {
        val filePath = System.getProperty("user.dir")
        val file = File("$filePath/src/main/public$path")
        return file.readText(Charsets.UTF_8)
    }

    fun extractPath(request: String): String {
        return request.split("\r\n")[0].split(" ")[1]
    }

    fun extractContentLength(request: String): Int {
        request.split("\n").forEach { headerString ->
            val keyValue = headerString.split(":", limit = 2)
            if (keyValue[0].contains("Content-Length")) {
                return keyValue[1].trim().toInt()
            }
        }
        return 0
    }

    fun extractBody(bodySize: Int, inputStream: BufferedReader): String {
        val buffer = CharArray(bodySize)
        inputStream.read(buffer)
        return String(buffer)
    }

    fun extractStatusCode(path: String): StatusCodes {
        if (path == "/index.html" || path == "/main.js" || path == "/main.css") {
            return StatusCodes.TWOHUNDRED
        }
        return StatusCodes.FOURHUNDREDFOUR
    }

}