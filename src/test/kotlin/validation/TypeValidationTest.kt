package validation

import com.google.gson.Gson
import jsonTemplate.configurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

class TypeValidationTest {

    private val typeValidation = TypeValidation()
    @Test
    fun shouldPerformTypeValidationCheck() {

        val metaData = """[{"fieldName": "Product Id","type": "AlphaNumeric","length": 4},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabet"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Product Id": "1564","Price": "4500.59","Export": "N"},{"Product Id": "1565","Price": "1000abc","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expectedError = """[{"2":"Incorrect Type of Price. Please change to Number"}]"""
        val expectedErrorList = JSONArray(expectedError)

        val actualErrorList = typeValidation.typeCheck(jsonCsvData ,postRouteHandler.fieldArray)

        Assertions.assertEquals(expectedErrorList.toString(), actualErrorList.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithMultipleErrors() {
        val metaData = """[{"fieldName": "Product Id","type": "AlphaNumeric","length": 5},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabet"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Product Id": "1564","Price": "4500.59a","Export": "N"},{"Product Id": "1565","Price": "1000abc","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = JSONArray("[{\"1\":\"Incorrect Type of Price. Please change to Number\"},{\"2\":\"Incorrect Type of Price. Please change to Number\"}]")

        val actual = typeValidation.typeCheck(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithNoErrors() {
        val metaData = """[{"fieldName": "Product Id","type": "AlphaNumeric","length": 5},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabet"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Product Id": "1564","Price": "4500.59","Export": "N"},{"Product Id": "1565","Price": "1000","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = JSONArray("[]")

        val actual = typeValidation.typeCheck(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldBeAbleToCheckIfValueIsNumeric() {
        val typeValidation = TypeValidation()
        val value = "1234"

        val actual = typeValidation.isNumeric(value)

        assertTrue(actual)
    }

    @Test
    fun shouldBeAbleToCheckIfValueIsAlphabetic() {
        val typeValidation = TypeValidation()
        val value = "shikha"

        val actual = typeValidation.isAlphabetic(value)

        assertTrue(actual)
    }

    @Test
    fun shouldBeAbleToCheckIfValueIsAlphaNumeric() {
        val typeValidation = TypeValidation()
        val value = "shikha123"

        val actual = typeValidation.isAlphaNumeric(value)

        assertTrue(actual)
    }
}

private fun getMetaData(body: String): Array<configurationTemplate> {
    val gson = Gson()
    return gson.fromJson(body, Array<configurationTemplate>::class.java)
}