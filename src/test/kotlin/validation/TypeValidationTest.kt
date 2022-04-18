package validation

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import routeHandler.PostRouteHandler
import java.util.stream.Stream

@Suppress("NAME_SHADOWING")
class TypeValidationTest {

    private val typeValidation = TypeValidation()

    @Test
    fun shouldPerformTypeValidationCheck() {

        val metaData =
            """[{"fieldName": "Product Id","type": "AlphaNumeric","length": 4},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabet"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "1564","Price": "4500.59","Export": "N"},{"Product Id": "1565","Price": "1000abc","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expectedError = """[{"2":"Incorrect Type of Price. Please change to Number"}]"""
        val expectedErrorList = JSONArray(expectedError)

        val actualErrorList = typeValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expectedErrorList.toString(), actualErrorList.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithMultipleErrors() {
        val metaData =
            """[{"fieldName": "Product Id","type": "AlphaNumeric","length": 5},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabet"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "1564","Price": "4500.59a","Export": "N"},{"Product Id": "1565","Price": "1000abc","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected =
            JSONArray("[{\"1\":\"Incorrect Type of Price. Please change to Number\"},{\"2\":\"Incorrect Type of Price. Please change to Number\"}]")

        val actual = typeValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithNoErrors() {
        val metaData =
            """[{"fieldName": "Product Id","type": "AlphaNumeric","length": 5},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabet"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "1564","Price": "4500.59","Export": "N"},{"Product Id": "1565","Price": "1000","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = JSONArray("[]")

        val actual = typeValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

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

    @ParameterizedTest
    @MethodSource("checkDateTimeFormatsWithGivenFormats")
    fun shouldBeAbleToCheckIfValueIsInDateTimeFormat(dateTimeFormat: String, dateTimeValue: String) {
        val typeValidation = TypeValidation()

        val actual = typeValidation.checkDateTimeFormat(dateTimeFormat, dateTimeValue)

        assertTrue(actual)
    }

    @Test
    fun shouldBeAbleToCheckIfValueIsNotInDateTimeFormat() {
        val typeValidation = TypeValidation()
        val value = "31-011-2012"
        val dateTimeFormat = "dd-MM-yyyy"

        val actual = typeValidation.checkDateTimeFormat(dateTimeFormat, value)

        assertFalse(actual)
    }

    companion object {
        @JvmStatic
        fun checkDateTimeFormatsWithGivenFormats(): Stream<Arguments> = Stream.of(
            Arguments.of("MM-dd-yyyy", "01-02-2018"),
            Arguments.of("HH:mm:ss.SSSZ", "13:03:15.454+0530"),
            Arguments.of("MMMM dd, yy", "February 17, 2009"),
            Arguments.of("yy/MM/dd", "2009/02/17"),
            Arguments.of("dd/MM/yy", "17/02/2009"),
            Arguments.of("MMM dd, yyyy hh:mm:ss a", "Dec 2, 2017 2:39:58 AM"),
            Arguments.of("dd/MMM/yyyy:HH:mm:ss ZZZZ", "19/Apr/2017:06:36:15 -0700"),
            Arguments.of("MMM dd HH:mm:ss ZZZZ yyyy", "Jan 21 18:20:11 +0000 2017"),
            Arguments.of("MMM dd yyyy HH:mm:ss", "Jun 09 2018 15:28:14"),

            )
    }


}

private fun getMetaData(body: String): Array<ConfigurationTemplate> {
    val gson = Gson()
    return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
}