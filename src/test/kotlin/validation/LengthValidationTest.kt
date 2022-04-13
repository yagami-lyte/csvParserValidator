package validation

import com.google.gson.Gson
import jsonTemplate.JsonMetaDataTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

internal class LengthValidationTest {

    private val lengthValidation = LengthValidation()

    @Test
    fun shouldPerformLengthValidationCheck() {
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"3","dependentOn":"Export","dependentValue":"N","values":["IND","USA","AUS"]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"INDIA"},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = JSONArray("[{\"1\":\"Incorrect length of Country Name. Please change its length to 3\"}]")

        val actual = lengthValidation.validateLength(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithMultipleErrors() {
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"2","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"3","dependentOn":"Export","dependentValue":"N","values":["IND","USA","AUS"]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Export":"Y","Country Name":""},{"Export":"Y","Country Name":""},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected =
            JSONArray("[{\"1\":\"Incorrect length of Export. Please change its length to 2\"},{\"2\":\"Incorrect length of Export. Please change its length to 2\"},{\"3\":\"Incorrect length of Export. Please change its length to 2\"}]")

        val actual = lengthValidation.validateLength(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithNoErrors() {
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"3","dependentOn":"Export","dependentValue":"N","values":["IND","USA","AUS"]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Export":"Y","Country Name":"AUS"},{"Export":"Y","Country Name":"IND"},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = JSONArray("[]")

        val actual = lengthValidation.validateLength(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }
}

private fun getMetaData(body: String): Array<JsonMetaDataTemplate> {
    val gson = Gson()
    return gson.fromJson(body, Array<JsonMetaDataTemplate>::class.java)
}