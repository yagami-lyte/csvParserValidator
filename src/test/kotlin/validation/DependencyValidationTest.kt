package validation

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

internal class DependencyValidationTest {

    private val dependencyValidation = DependencyValidation()

    @Test
    fun shouldPerformDependencyCheck() {
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":""},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = mutableMapOf(
            "Country Name" to mutableListOf(1)
        )
        val actual = dependencyValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithMultipleErrors() {
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"2","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Export":"Y","Country Name":""},{"Export":"Y","Country Name":""},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val dependencyValidation = DependencyValidation()
        val expected = mutableMapOf(
            "Country Name" to mutableListOf(1,2)
        )

        val actual = dependencyValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithNoErrors() {
        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"3","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Export":"Y","Country Name":"AUS"},{"Export":"Y","Country Name":"IND"},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val dependencyValidation = DependencyValidation()
        val expected = mutableMapOf<String,MutableList<Int>>()

        val actual = dependencyValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }
}

private fun getMetaData(body: String): Array<ConfigurationTemplate> {
    val gson = Gson()
    return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
}

