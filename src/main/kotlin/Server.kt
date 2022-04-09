import arrow.core.split
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket

class Server(private val port : Int = 3032) {

    private val serverSocket = ServerSocket(port)

    fun startServer() {
        while(true) {
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
        if(methodType == "GET") {
            return handleGetRequest(request)
        }
        TODO()
    }

    fun handleGetRequest(request: String): String {
        return "GetRequest"
    }

    private fun readRequest(inputStream: BufferedReader): String {
        var request = ""
        var flag = true
        while(flag) {
            val line = inputStream.readLine()
            request += line + "\r\n"
            if(line.isEmpty()){
                flag = false
            }
        }
        return request
    }

    fun get():Int{
        return 1
    }

}
