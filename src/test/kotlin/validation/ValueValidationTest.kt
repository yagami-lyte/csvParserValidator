package validation

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import routeHandler.postRouteHandler.PostRouteHandler

internal class ValueValidationTest {

    private val valueValidation = ValueValidation()
    @Test
    fun shouldPerformLengthValidationCheck() {
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"3","dependentOn":"Export","dependentValue":"N","values":["IND","USA","AUS"]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"INDIA"},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = mutableMapOf(
            "Country Name" to mutableListOf(2)
        )

        val actual = valueValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithMultipleErrors() {
        val metaData = """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"3","dependentOn":"Export","dependentValue":"N","values":["IND","USA","AUS"]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"INDIA"},{"Export":"N","Country Name":"CHI"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = mutableMapOf(
            "Country Name" to mutableListOf(2,3)
        )

        val actual = valueValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithNoErrors() {
        val metaData = """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"3","dependentOn":"Export","dependentValue":"Y","values":["IND","USA","AUS"]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"AUS"},{"Export":"Y","Country Name":"IND"},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = mutableMapOf<String , MutableList<Int>>()

        val actual = valueValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnTrueIfValueIsPresent() {
        val valueValidation = ValueValidation()
        val allowedValues = listOf("IND", "USA", "AUS")
        val value = "USA"

        val actual = valueValidation.valueCheck(allowedValues, value)

        assertTrue(actual)
    }

    @Test
    fun shouldReturnFalseIfValueIsNotPresent() {
        val valueValidation = ValueValidation()
        val allowedValues = listOf("IND", "USA", "AUS")
        val value = "CHINA"

        val actual = valueValidation.valueCheck(allowedValues, value)

        assertFalse(actual)
    }
}

private fun getMetaData(body: String): Array<ConfigurationTemplate> {
    val gson = Gson()
    return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
}