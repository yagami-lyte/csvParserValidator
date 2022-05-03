package validation

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

internal class PrependingZeroesValidationTest {

    private val prependingZeroesValidation = PrependingZeroesValidation()

    @Test
    fun shouldPerformPrependingZeroesCheck() {
        val metaData =
            """[{"fieldName":"Export Number","type":"Number","length":"","dependentOn":"","dependentValue":"","values":[]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export Number":"0034","Country Name":""},{"Export Number":"12","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected =
            JSONArray("[{\"1\":\"Incorrect Type of Export Number. Please remove Pre-pending Zeros!\"}]")

        val actual = prependingZeroesValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldPerformPrependingZeroesCheckWithMultipleErrors() {
        val metaData =
            """[{"fieldName":"Export Number","type":"Number","length":"","dependentOn":"","dependentValue":"","values":[]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export Number":"0034","Country Name":""},{"Export Number":"012","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected =
            JSONArray("[{\"1\":\"Incorrect Type of Export Number. Please remove Pre-pending Zeros!\"},{\"2\":\"Incorrect Type of Export Number. Please remove Pre-pending Zeros!\"}]")

        val actual = prependingZeroesValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldPerformPrependingZeroesCheckWithNoErrors() {
        val metaData =
            """[{"fieldName":"Export Number","type":"Number","length":"","dependentOn":"","dependentValue":"","values":[]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export Number":"34","Country Name":""},{"Export Number":"12","Country Name":"USA"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected =
            JSONArray("[]")

        val actual = prependingZeroesValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnTrueForPrependingZeroesValues(){
        val prependingZeroesValidation = PrependingZeroesValidation()
        val value = "0123"

        val expected = prependingZeroesValidation.checkPrePendingZeros(value)

        assertTrue(expected)
    }

    @Test
    fun shouldFalseTrueIfValueDoesNotHavePrependingZeroes(){
        val prependingZeroesValidation = PrependingZeroesValidation()
        val value = "123"

        val expected = prependingZeroesValidation.checkPrePendingZeros(value)

        assertFalse(expected)
    }

    private fun getMetaData(body: String): Array<ConfigurationTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
    }
}
