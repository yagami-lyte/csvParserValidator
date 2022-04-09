import java.io.*
import java.net.ServerSocket
import routeHandler.GetRouteHandler
import routeHandler.PostRouteHandler

class Server(private val port: Int) {

    private val serverSocket = ServerSocket(port)
    var fieldArray: Array<JsonMetaDataTemplate> = arrayOf()
    private val getRouteHandler = GetRouteHandler()
    private val postRouteHandler = PostRouteHandler()


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
        val response = handleRequest(request, inputStream)

        sendResponse(outputStream, response)
        clientSocket.close()
    }

    private fun sendResponse(outputStream: BufferedWriter, response: String) {
        outputStream.write(response)
        outputStream.flush()
    }

    private fun handleRequest(request: String, inputStream: BufferedReader): String {
        val methodType = request.split("\r\n")[0].split(" ")[0]
        return when (methodType) {
            "GET" -> getRouteHandler.handleGetRequest(request)
            "POST" -> postRouteHandler.handlePostRequest(request, inputStream)
            else -> handleUnknownRequest()
        }
    }

    private fun handleUnknownRequest(): String {
        val httpHead = "HTTP/1.1 400 Bad Request"
        val endOfHeader = "\r\n\r\n"
        return httpHead + endOfHeader
    }



    private fun readRequest(inputStream: BufferedReader): String {
        var request = ""
        var flag = true
        while (flag) {
            val line = inputStream.readLine()
            request += line + "\r\n"
            if (line == null || line.isEmpty()) {
                flag = false
            }
        }
        return request
    }
}
