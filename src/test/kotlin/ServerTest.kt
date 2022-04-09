import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ServerTest {

    @Test
    fun shouldProcessGetRequest(){
        val server = Server()
        val expected = 1

        val actual =server.get()

        assertEquals(expected, actual)
    }
}