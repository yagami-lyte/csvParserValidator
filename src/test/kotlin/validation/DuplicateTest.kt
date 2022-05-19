package validation

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

class DuplicateTest {

    private val metaData =
        """[{"fieldName":"Export","type":"Alphabets","length":"3","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""

    @Test
    fun shouldReturnEmptyMapForNoDuplicates() {
        val duplicateValidation = DuplicateValidation()
        val jsonString = "[{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 4}]"
        val jsonArray = JSONArray(jsonString)
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData

        val actual = duplicateValidation.validate(jsonArray,postRouteHandler.fieldArray)

        assertTrue(actual.isEmpty())
    }

    @Test
    fun shouldReturnDuplicatePairsIfPresent() {
        val duplicateValidation = DuplicateValidation()
        val jsonString = "[{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 3}]"
        val jsonArray = JSONArray(jsonString)
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val expected = mutableMapOf(
            "2" to mutableListOf(1)
        )

        val actual = duplicateValidation.validate(jsonArray, postRouteHandler.fieldArray)

        assertEquals(expected.toList(), actual.toList())
    }

    @Test
    fun shouldReturnAllDuplicatePairsIfPresent() {
        val duplicateValidation = DuplicateValidation()
        val jsonString = "[{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 3} ]"
        val jsonArray = JSONArray(jsonString)
        val expected = mutableMapOf(
            "2" to listOf(1),
            "3" to listOf(1)
        )
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData

        val actual = duplicateValidation.validate(jsonArray , postRouteHandler.fieldArray)

        assertEquals(expected.toList(), actual.toList())
    }

    @Test
    fun shouldReturnDifferentDuplicatePairsIfPresent() {
        val duplicateValidation = DuplicateValidation()
        val jsonString = "[{a : 1, b : 2, c : 3},{a : 1, b : 2, c : 3},{a : 2, b : 2, c : 2}, {a : 2, b : 2, c : 2}]"
        val jsonArray = JSONArray(jsonString)
        val expected = mutableMapOf(
            "2" to mutableListOf(1),
            "4" to mutableListOf(3)
        )
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData

        val actual = duplicateValidation.validate(jsonArray , postRouteHandler.fieldArray)

        assertEquals(expected.toList(), actual.toList())
    }

    private fun getMetaData(body: String): Array<ConfigurationTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
    }

}