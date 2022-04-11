package validation

import org.json.JSONArray
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

internal class DependencyValidationTest {

    private val metaData = """[
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
    "fieldName": "Country Name",
    "type": "Alphabet",
    "length": 3,
    "values": [
      "IND",
      "USA",
      "AUS"
    ],
    "dependentOn": "Export",
    "dependentValue": "N"
  },
  {
    "fieldName": "Export",
    "type": "Alphabet",
    "values": [
      "Y",
      "N"
    ]
  }
]"""

    @Test
    fun shouldPerformDependencyCheck() {

        val postRouteHandler = PostRouteHandler()
        val jsonData = postRouteHandler.getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[
    {
        "Product Id": "1234",
        "Product Description": "Chairs",
        "Price": "1000abc",
        "Export": "N",
        "Country Name": "AUS",
        "Source City": "Mumbai",
        "Country Code": "61"
    },
    
]"""

        val jsonCsvData = JSONArray(csvData)
        val dependencyValidation = DependencyValidation(jsonCsvData, postRouteHandler.fieldArray)
        val expected = JSONArray("[{\"1\":\"Value of Export is N, Hence Country Name should be empty\"}]")
        val actual = dependencyValidation.dependencyCheck()

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithMultipleErrors() {

        val postRouteHandler = PostRouteHandler()
        val jsonData = postRouteHandler.getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[
    {
        "Product Id": "1234",
        "Product Description": "Chairs",
        "Price": "1000abc",
        "Export": "N",
        "Country Name": "IND",
        "Source City": "Mumbai",
        "Country Code": "61"
    },
    {
        "Product Id": "1234",
        "Product Description": "Chairs",
        "Price": "1000abc",
        "Export": "N",
        "Country Name": "AUS",
        "Source City": "Mumbai",
        "Country Code": "61"
    },
    
]"""

        val jsonCsvData = JSONArray(csvData)
        val dependencyValidation = DependencyValidation(jsonCsvData, postRouteHandler.fieldArray)
        val expected =
            JSONArray("[{\"1\":\"Value of Export is N, Hence Country Name should be empty\"},{\"2\":\"Value of Export is N, Hence Country Name should be empty\"}]]")
        val actual = dependencyValidation.dependencyCheck()

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithNoErrors() {

        val postRouteHandler = PostRouteHandler()
        val jsonData = postRouteHandler.getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[
    {
        "Product Id": "1234",
        "Product Description": "Chairs",
        "Price": "1000abc",
        "Export": "Y",
        "Country Name": "IND",
        "Source City": "Mumbai",
        "Country Code": "61"
    }
    
]"""

        val jsonCsvData = JSONArray(csvData)
        val dependencyValidation = DependencyValidation(jsonCsvData, postRouteHandler.fieldArray)
        val expected = JSONArray("[]")
        val actual = dependencyValidation.dependencyCheck()

        assertEquals(expected.toString(), actual.toString())
    }
}

