package routeHandler

import org.json.JSONArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PostRouteHandlerTest {

    @Test
    fun shouldBeAbleToAddCSVMetaData() {
        val postRouteHandler = PostRouteHandler()
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
        postRouteHandler.addCsvMetaData(data)
        val field = postRouteHandler.fieldArray[0]
        Assertions.assertNull(field.maxLength)
        assertEquals(5, field.length)
        assertEquals("Number", postRouteHandler.fieldArray[2].type)
    }

    @Test
    fun shouldBeAbleToReturnLengthErrorIndices(){
        val metaData = """[
  {
    "fieldName": "Product Id",
    "type": "AlphaNumeric",
    "length": 5
  },
  {
    "fieldName": "Product Description",
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
    "type": "Alphabet"
  },
  {
    "fieldName": "Country Name",
    "type": "Alphabet",
    "minLength": 3
  },
  {
    "fieldName": "Source City",
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
        val postRouteHandler = PostRouteHandler()
        val jsonData = postRouteHandler.getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData ="""[
    {
        "Product Id": "1564",
        "Product Description": "Table",
        "Price": "4500.59",
        "Export": "N",
        "Source City": "Nagpur",
        "Source Pincode": "440001"
    },
    {
        "Product Id": "12345",
        "Product Description": "Chairs",
        "Price": "1000",
        "Export": "Y",
        "Country Name": "AUS",
        "Source City": "Mumbai",
        "Country Code": "61",
        "Source Pincode": "400001"
    }
]"""
        val jsonCsvData = JSONArray(csvData)
        val expectedErrorList = mutableListOf(1,2)

        val actualErrorResult = postRouteHandler.lengthValidation(jsonCsvData)

        assertEquals(expectedErrorList,actualErrorResult)
    }












}