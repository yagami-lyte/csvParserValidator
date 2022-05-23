package routeHandler.getRouteHandler.getResponse

import database.DatabaseOperations
import database.TestConnector
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ConfigNamesTest {
    private val testConfigurationName1 = "testConfigurationName1"
    private val testConfigurationName2 = "testConfigurationName2"

    @Test
    fun shouldBeAbleToGetConfigurationNamesForConfigNameGetRequest() {
        val configNamesPath = "/get-config-files"
        val databaseOperations = DatabaseOperations(TestConnector())
        val testConfigurationName1 = "testConfigurationName1"
        databaseOperations.saveNewConfigurationInDatabase(testConfigurationName1)
        val configNames = ConfigNames(databaseOperations)
        val expectedConfigNames = """{"configFiles" : {"0":"testConfigurationName1"}}"""
        val response = configNames.getResponse(configNamesPath)

        val actualConfigNames = response.split("\r\n")[2]

        assertEquals(expectedConfigNames, actualConfigNames)
    }

    @Test
    fun shouldBeAbleToGetMultipleConfigurationNamesForConfigNameGetRequest() {
        val configNamesPath = "/get-config-files"
        val databaseOperations = DatabaseOperations(TestConnector())
        databaseOperations.saveNewConfigurationInDatabase(testConfigurationName1)
        databaseOperations.saveNewConfigurationInDatabase(testConfigurationName2)
        val configNames = ConfigNames(databaseOperations)
        val expectedConfigNames = """{"configFiles" : {"0":"testConfigurationName1","1":"testConfigurationName2"}}"""
        val response = configNames.getResponse(configNamesPath)

        val actualConfigNames = response.split("\r\n")[2]

        assertEquals(expectedConfigNames, actualConfigNames)
    }

    @Test
    fun shouldBeAbleToGetZeroConfigurationNamesForConfigNameGetRequest() {
        val configNamesPath = "/get-config-files"
        val databaseOperations = DatabaseOperations(TestConnector())
        val configNames = ConfigNames(databaseOperations)
        val expectedConfigNames = """{"configFiles" : {}}"""
        val response = configNames.getResponse(configNamesPath)

        val actualConfigNames = response.split("\r\n")[2]

        assertEquals(expectedConfigNames, actualConfigNames)
    }


}