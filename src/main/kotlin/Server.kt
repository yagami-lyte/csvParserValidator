
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import com.google.gson.Gson

class Server(private val port: Int = 3032) {

    private val serverSocket = ServerSocket(port)
    var fieldArray: Array<JsonMetaDataTemplate> = arrayOf()

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
        val response = handleRequest(request , inputStream)

        sendResponse(outputStream, response)
        clientSocket.close()
    }

    private fun sendResponse(outputStream: BufferedWriter, response: String) {
        outputStream.write(response)
        outputStream.flush()
    }

    private fun handleRequest(request: String , inputStream: BufferedReader): String {
        val methodType = request.split("\r\n")[0].split(" ")[0]
        return when (methodType) {
            "GET" -> handleGetRequest(request)
            "POST" -> handlePostRequest(request , inputStream)
            else -> handleUnknownRequest()
        }
    }

    private fun handleUnknownRequest(): String {
        val httpHead = "HTTP/1.1 400 Bad Request"
        val endOfHeader = "\r\n\r\n"
        return httpHead + endOfHeader
    }

    fun handlePostRequest(request: String , inputStream: BufferedReader): String {
        val path = request.split("\r\n")[0].split(" ")[1]
        if(path == "/add-meta-data") {
            handleAddMetaData(inputStream)
        }
        TODO()
    }

    private fun handleAddMetaData(inputStream: BufferedReader): String {
        val body = getBody(inputStream)
        return addMetaData(body)
    }

    fun addMetaData(body: String): String {
        val gson = Gson()
        val jsonBody =  gson.fromJson(body, Array<JsonMetaDataTemplate>::class.java)

        fieldArray = jsonBody

        val endOfHeader = "\r\n\r\n"
        val responseBody = "Successfully Added"
        val contentLength = responseBody.length
        val httpHead = "HTTP/1.1 200 Found"
        return httpHead + """Content-Type: text/plain; charset=utf-8
            |Content-Length: $contentLength""".trimMargin() + endOfHeader + responseBody
    }

    private fun getBody(inputStream: BufferedReader): String {
        var body = ""
        var line = inputStream.readLine()
        while (line != null) {
            body += line
            line = inputStream.readLine()
        }
        return body
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
        var file = File("$filePath/src/main/public/$path")
        if (file.exists() === false) {
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
