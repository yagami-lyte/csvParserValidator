import arrow.core.split
import java.io.*
import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int = 3032) {

    private val serverSocket = ServerSocket(port)

    fun startServer() {
        while (true) {
            makeConnection()
        }
    }

    private fun makeConnection() {
        val clientSocket = serverSocket.accept()
        val outputStream = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
        val inputStream = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

        val request = readRequest(inputStream)
        val response = handleRequest(request)

        outputStream.write(response)
        outputStream.flush()
    }

    private fun handleRequest(request: String): String {
        val methodType = request.split("\r\n")[0].split(" ")[0]
        if (methodType == "GET") {
            return handleGetRequest(request)
        }
        TODO()
    }

    fun handleGetRequest(request: String): String {
        val path = request.split("\r\n")[0].split(" ")[1]
        if (path == "/") {
            return getResponse("/index.html")
        }
        return getResponse("/404.html")
    }

    private fun getResponse(path: String): String {
        val filePath = System.getProperty("user.dir")
        var file = File("$filePath/src/main/public$path")
        if (file.exists() == false) {
            var file = File("$filePath/src/main/public/404.html")
        }
        val bodyResponse = file.readText(Charsets.UTF_8)
        val httpHead = "HTTP/1.1 200 Found"
        val contentLength = bodyResponse.length
        return httpHead + """Content-Type: text/html; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + "\r\n\r\n" + bodyResponse

    }

    private fun readRequest(inputStream: BufferedReader): String {
        var request = ""
        var flag = true
        while (flag) {
            val line = inputStream.readLine()
            request += line + "\r\n"
            if (line.isEmpty()) {
                flag = false
            }
        }
        return request
    }


}
