package routeHandler

import JsonMetaDataTemplate
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PostRouteHandlerTest {

    @Test
    fun shouldBeAbleToGetResponseForCsvPOSTRequest() {

        val metaData =
            """[{"fieldName":"Export","type":"Alphabets","length":"1","dependentOn":"","dependentValue":"","values":["Y","N"]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        val postRouteHandler = PostRouteHandler()
        val jsonData = getMetaData(metaData)
        postRouteHandler.fieldArray = jsonData
        val csvData = """[{"Export":"Y","Country Name":"INDIA"},{"Export":"N","Country Name":"USA"}]"""
        val expectedErrorResponse = """{"Duplicates" : [],"Length" : [{"1":"Incorrect length of Country Name. Please change its length to 4"},{"2":"Incorrect length of Country Name. Please change its length to 4"}],"Type" : [],"Value" : [{"1":"Incorrect Value of Country Name. Please select value from [Export,Country Name, Y,, N,USA, ]"},{"2":"Incorrect Value of Country Name. Please select value from [Export,Country Name, Y,, N,USA, ]"}],"Dependency" : []}"""
        val response = postRouteHandler.getResponseForCSV(csvData)

        val actualErrorResponse = response.split("\r\n\r\n")[1]
        println(actualErrorResponse)

        assertEquals(expectedErrorResponse,actualErrorResponse)
    }
}

private fun getMetaData(data: String): Array<JsonMetaDataTemplate> {
    val gson = Gson()
    return gson.fromJson(data, Array<JsonMetaDataTemplate>::class.java)
}


