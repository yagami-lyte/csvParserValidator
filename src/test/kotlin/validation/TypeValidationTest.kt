package validation

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.provider.Arguments
import routeHandler.postRouteHandler.PostRouteHandler
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Suppress("NAME_SHADOWING")
class TypeValidationTest {

    private val typeValidation = TypeValidation()

    companion object {
        @JvmStatic
        fun checkDateTimeFormatsWithInvalidFormats(): Stream<Arguments> = Stream.of(
            Arguments.of("MM-dd-yyyy", "01  2-218"),
            Arguments.of("HH:mm:ss.SSS", "13:0.454+0530"),
            Arguments.of("MMMM dd, yy", "February 17 2009"),
            Arguments.of("yy/MM/dd", "2009/0217"),
            Arguments.of("dd/MM/yy", "17/ 022009"),
            Arguments.of("MMM dd, yyyy hh:mm:ss a", "Dec , 2017 2:39:58 AM"),
            Arguments.of("dd/MMM/yyyy:HH:mm:ss ZZZZ", "19/ /2017:  06:36:15 -0700"),
            Arguments.of("MM/dd/yyyy hh:mm:ss a", "9/28/2011 2/ 23:15 PM"),
            Arguments.of("MM/dd/yyyy hh:mm:ss a", "9/28011 2:23:15 PM"),
            Arguments.of("MM/dd/yyyy hh:mm:ss a:SSS", "8/511 3:31:18 AM:234"),
            Arguments.of("MMdd_HH:mm:ss.SSS", "0423_::11:42:35.883"),
            Arguments.of("MMdd_HH:mm:ss", "04231:42:35"),
            Arguments.of("dd MMM yyyy HH:mm:ss*SSS", "23 Apr 2017 10//32:35*311"),
            Arguments.of("dd MMM yyyy HH:mm:ss", "23 Apr 2017 11/42:35"),
            Arguments.of("dd-MMM-yyyy HH:mm:ss", "23-Apr::2017 11:42:35"),
            Arguments.of("dd/MMM/yyyy HH:mm:ss", "23/Apr/2017 11:42//35"),
            Arguments.of("dd/MMM HH:mm:ss,SSS", "23Apr 11:4235,173"),
            Arguments.of("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "2000-07-205:06:07.00"),
            Arguments.of("yyyy-MM-dd HH:mm:ss ZZZZ", "17-08-19 2:17:55 -0  00"),
            Arguments.of("yyyy/MM/dd*HH:mm:ss", "2010412*19:37:50"),
            Arguments.of("yyyy MMM dd HH:mm:ss.SSS*zzz", "2018  13 22:08:13.211*PDT"),
        )

        @JvmStatic
        fun checkDateTimeFormatsWithValidFormats(): Stream<Arguments> = Stream.of(
            Arguments.of("MM-dd-yyyy", "01-02-2018"),
            Arguments.of("HH:mm:ss.SSSZ", "13:03:15.454+0530"),
            Arguments.of("MMMM dd, yy", "February 17, 2009"),
            Arguments.of("yy/MM/dd", "2009/02/17"),
            Arguments.of("dd/MM/yy", "17/02/2009"),
            Arguments.of("MMM dd, yyyy hh:mm:ss a", "Dec 2, 2017 2:39:58 AM"),
            Arguments.of("dd/MMM/yyyy:HH:mm:ss ZZZZ", "19/Apr/2017:06:36:15 -0700"),
            Arguments.of("MMM dd HH:mm:ss ZZZZ yyyy", "Jan 21 18:20:11 +0000 2017"),
            Arguments.of("MMM dd yyyy HH:mm:ss", "Jun 09 2018 15:28:14"),
            Arguments.of("MM/dd/yyyy hh:mm:ss a", "9/28/2011 2:23:15 PM"),
            Arguments.of("MM/dd/yyyy hh:mm:ss a", "9/28/2011 2:23:15 PM"),
            Arguments.of("MM/dd/yyyy hh:mm:ss a:SSS", "8/5/2011 3:31:18 AM:234"),
            Arguments.of("MMdd_HH:mm:ss.SSS", "0423_11:42:35.883"),
            Arguments.of("MMdd_HH:mm:ss", "0423_11:42:35"),
            Arguments.of("dd MMM yyyy HH:mm:ss*SSS", "23 Apr 2017 10:32:35*311"),
            Arguments.of("dd MMM yyyy HH:mm:ss", "23 Apr 2017 11:42:35"),
            Arguments.of("dd-MMM-yyyy HH:mm:ss", "23-Apr-2017 11:42:35"),
            Arguments.of("dd-MMM-yyyy HH:mm:ss", "23-Apr-2017 11:42:35"),
            Arguments.of("dd/MMM/yyyy HH:mm:ss", "23/Apr/2017 11:42:35"),
            Arguments.of("dd/MMM/yyyy HH:mm:ss", "23/Apr/2017 11:42:35"),
            Arguments.of("dd/MMM HH:mm:ss,SSS", "23/Apr 11:42:35,173"),
            Arguments.of("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "2000-07-21T05:06:07.001Z"),
            Arguments.of("yyyy-MM-dd HH:mm:ss ZZZZ", "2017-08-19 12:17:55 -0400"),
            Arguments.of("yyyy-MM-dd HH:mm:ss,SSS", "2017-06-26 02:31:29,573"),
            Arguments.of("yyyy/MM/dd*HH:mm:ss", "2017/04/12*19:37:50"),
            Arguments.of("yyyy MMM dd HH:mm:ss.SSS*zzz", "2018 Apr 13 22:08:13.211*PDT"),
            Arguments.of("yyyy-MM-dd'T'HH:mm:ss", "2000-07-21T05:06:07"),
        )

    }


    @Test
    fun shouldPerformTypeValidationCheck() {

        val metaData =
            """[{"fieldName": "Product Id","type": "Text","length": 4},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabets"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "s@gmail,com","Price": "4500.59","Export": "N"},{"Product Id": "s@gmail,com","Price": "1000abc","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expectedError = mutableMapOf(
            "Price" to mutableListOf(3)
        )

        val actualErrorList = typeValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expectedError.toString(), actualErrorList.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithMultipleErrors() {
        val metaData =
            """[{"fieldName": "Product Id","type": "AlphaNumeric","length": 5},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabets"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "1564","Price": "4500.59a","Export": "N"},{"Product Id": "1565","Price": "1000abc","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = mutableMapOf(
            "Price" to mutableListOf(2,3)
        )

        val actual = typeValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun shouldReturnJsonArrayWithNoErrors() {
        val metaData =
            """[{"fieldName": "Product Id","type": "AlphaNumeric","length": 5},{"fieldName": "Price","type": "Number"},{"fieldName": "Export","type": "Alphabets"}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData =
            """[{"Product Id": "1564","Price": "4500.59","Export": "N"},{"Product Id": "1565","Price": "1000","Export": "Y"}]"""
        val jsonCsvData = JSONArray(csvData)
        val expected = mutableMapOf<String , MutableList<Int>>()

        val actual = typeValidation.validate(jsonCsvData, postRouteHandler.fieldArray)

        Assertions.assertEquals(expected.toString(), actual.toString())
    }

    private fun getMetaData(body: String): Array<ConfigurationTemplate> {
        val gson = Gson()
        return gson.fromJson(body, Array<ConfigurationTemplate>::class.java)
    }
}

