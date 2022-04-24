package validation

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import routeHandler.PostRouteHandler

class NullValidationTest {

    private val nullValidation = NullValidation()

    @Test
    fun shouldCheckIfNullValuesAreNotAllowed() {

        val metaData =
            """[{"fieldName": "Product Id","type": "Special Characters","length": 4,"nullValue":"Not Allowed"},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabet"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "","Price": "4500.59","Export": "N"},{"Product Id": "s@gmail,com","Price": "1000abc","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expectedError = """[{"1":"Has empty value for Product Id. Please enter a value in your CSV"}]"""
        val expectedErrorList = JSONArray(expectedError)

        val actualErrorList = nullValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expectedErrorList.toString(), actualErrorList.toString())
    }

    @Test
    fun shouldCheckIfNullValuesAreAllowed() {
        val metaData =
            """[{"fieldName": "Product Id","type": "Special Characters","length": 4},{"fieldName": "Price","type": "Number", "nullValue":"Allowed"},{"fieldName": "Export","type": "Alphabet"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "1234","Price": "432","Export": "N"},{"Product Id": "s@gmail,com","Price": "1000abc","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expectedError = """[]"""
        val expectedErrorList = JSONArray(expectedError)

        val actualErrorList = nullValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expectedErrorList.toString(), actualErrorList.toString())
    }

    @Test
    fun shouldCheckIfNullValuesInMultipleFieldsAreNotAllowed() {
        val metaData =
            """[{"fieldName": "Product Id","type": "Special Characters","length": 4},{"fieldName": "Price","type": "Number", "nullValue":"Not Allowed"},{"fieldName": "Export","type": "Alphabet", "nullValue":"Not Allowed"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "1234","Price": "432","Export": ""},{"Product Id": "s@gmail,com","Price": "","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expectedError = """[{"1":"Has empty value for Export. Please enter a value in your CSV"}, {"2":"Has empty value for Price. Please enter a value in your CSV"}]"""
        val expectedErrorList = JSONArray(expectedError)

        val actualErrorList = nullValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expectedErrorList.toString(), actualErrorList.toString())
    }

    @Test
    fun shouldCheckIfNullValuesInMultipleFieldsAreAllowed() {
        val metaData =
            """[{"fieldName": "Product Id","type": "Special Characters","length": 4},{"fieldName": "Price","type": "Number", "nullValue":"Allowed"},{"fieldName": "Export","type": "Alphabet", "nullValue":"Allowed"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "1234","Price": "432","Export": ""},{"Product Id": "s@gmail,com","Price": "","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expectedError = """[]"""

        val expectedErrorList = JSONArray(expectedError)

        val actualErrorList = nullValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expectedErrorList.toString(), actualErrorList.toString())
    }


}

private fun getMetaData(body: String): Array<ConfigurationTemplate> {
    val gson = Gson()
    return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
}
