package server

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.net.Socket

class ServerTest {

    @Test
    fun shouldReturnTrueIfTheServerHasStarted() {
        val port = 3000
        var actual = true
        Server(port)

        try {
            val socket = Socket("localhost",port)
            socket.close()
        } catch (e: Exception) {
            actual = false
        }

        assertTrue(actual)
    }

    @Test
    fun shouldReturnFalseIfTheServerHasNotStarted() {
        val port = 3010
        var actual = true

        try {
            val socket = Socket("localhost",port)
            socket.close()
        } catch (e: Exception) {
            actual = false
        }

        assertFalse(actual)
    }
}