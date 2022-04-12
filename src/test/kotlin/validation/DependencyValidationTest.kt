package validation

import org.json.JSONArray
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

internal class DependencyValidationTest {

    private val dependencyValidation = DependencyValidation()
    private val metaData = """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""

    @Test
    fun shouldPerformDependencyCheck() {

        val postRouteHandler = PostRouteHandler()
        val jsonData = postRouteHandler.getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":""},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = JSONArray("[{\"1\":\"Value of Country Name is dependent on Export.Do not leave Country Name empty.\"}]")

        val actual = dependencyValidation.dependencyValidation(jsonCsvData , postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithMultipleErrors() {

        val postRouteHandler = PostRouteHandler()
        val jsonData = postRouteHandler.getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":""},{"Export":"Y","Country Name":""},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val dependencyValidation = DependencyValidation()
        val expected = JSONArray("[{\"1\":\"Value of Country Name is dependent on Export.Do not leave Country Name empty.\"},{\"2\":\"Value of Country Name is dependent on Export.Do not leave Country Name empty.\"}]]")

        val actual = dependencyValidation.dependencyValidation(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithNoErrors() {

        val postRouteHandler = PostRouteHandler()
        val jsonData = postRouteHandler.getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"AUS"},{"Export":"Y","Country Name":"IND"},{"Export":"N","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val dependencyValidation = DependencyValidation()
        val expected = JSONArray("[]")

        val actual = dependencyValidation.dependencyValidation(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }
}

