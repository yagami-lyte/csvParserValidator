package server

import routeHandler.RouteHandler
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket

class Server(port: Int) {

    private val serverSocket = ServerSocket(port)
    private val routeHandler = RouteHandler()

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
        val methodType = getMethodType(request)
        val response = routeHandler.handleRequest(request, inputStream, methodType)

        sendResponse(outputStream, response)
        clientSocket.close()
    }

    private fun sendResponse(outputStream: BufferedWriter, response: String) {
        outputStream.write(response)
        outputStream.flush()
    }

    private fun getMethodType(request: String): String {
        return request.split("\r\n")[0].split(" ")[0]
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