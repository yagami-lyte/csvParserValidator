package validation

import com.google.gson.Gson
import jsonTemplate.configurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

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
        val expected = JSONArray("[{\"1\":\"Incorrect Value of Country Name. Please select value from [IND, USA, AUS]\"}]")

        val actual = valueValidation.validationCheck(jsonCsvData, postRouteHandler.fieldArray)

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
        val expected = JSONArray("[{\"1\":\"Incorrect Value of Country Name. Please select value from [IND, USA, AUS]\"},{\"2\":\"Incorrect Value of Country Name. Please select value from [IND, USA, AUS]\"}]")

        val actual = valueValidation.validationCheck(jsonCsvData, postRouteHandler.fieldArray)

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
        val expected = JSONArray("[]")

        val actual = valueValidation.validationCheck(jsonCsvData, postRouteHandler.fieldArray)

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

private fun getMetaData(body: String): Array<configurationTemplate> {
    val gson = Gson()
    return gson.fromJson(body, Array<configurationTemplate>::class.java)
}