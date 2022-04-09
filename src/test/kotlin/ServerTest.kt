import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ServerTest {

    @Test
    fun shouldBeAbleToGetResponseIfPathExist() {
        val server = Server()
        val request = """GET / HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        val expected = "1774"

        val response = server.handleGetRequest(request)
        val actual = "Content-Length: (.*)".toRegex().find(response)?.groupValues?.get(1)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldBeAbleToGetResponseIfPathNotExist() {
        val server = Server()
        val request = """GET /123 HTTP/1.1 
                |Host: localhost:3000""".trimMargin() + "\r\n\r\n"
        val expected = "164"

        val response = server.handleGetRequest(request)
        println(response)
        val actual = "Content-Length: (.*)".toRegex().find(response)?.groupValues?.get(1)

        assertEquals(expected, actual)
    }


    @Test
    fun shouldBeAbleToAddCSVMetaData() {
        val server = Server()
        val data = """[
      {
        "fieldName": "ProductId",
        "type": "AlphaNumeric",
        "length": 5
      },
      {
        "fieldName": "ProductDescription",
        "type": "AlphaNumeric",
        "minLength": 7,
        "maxLength": 20
      },
      {
        "fieldName": "Price",
        "type": "Number"
      },
      {
        "fieldName": "Export",
        "type": "Alphabet",
        "values": [
          "Y",
          "N"
        ]
      },
      {
        "fieldName": "Country Name",
        "type": "Alphabet",
        "minLength": 3
      },
      {
        "fieldName": "Source",
        "type": "Alphabet",
        "minLength": 3
      },
      {
        "fieldName": "Country Code",
        "type": "Number",
        "maxLength": 3
      },
      {
        "fieldName": "Source Pincode",
        "type": "Number",
        "length": 6,
        "values": [
          "500020",
          "110001",
          "560001",
          "500001",
          "111045",
          "230532",
          "530068",
          "226020",
          "533001",
          "600001",
          "700001",
          "212011",
          "641001",
          "682001",
          "444601"
        ]
      }
    ]"""
        server.addMetaData(data)
        val field = server.fieldArray[0]
        Assertions.assertNull(field.maxLength)
        assertEquals(5, field.length)
        assertEquals("Number", server.fieldArray[2].type)
    }
}