package database

import com.google.gson.Gson
import jsonTemplate.ConfigurationTemplate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DatabaseOperationsTest {

    @Test
    fun shouldBeAbleToReturnTrueIfConfigNameIsPresentInTheDatabase() {

        val databaseOperations = DatabaseOperations(TestConnector())
        val configName = "test4"
        databaseOperations.saveNewConfigurationInDatabase(configName)

        val result = databaseOperations.isConfigPresentInDatabase(configName)

        assertTrue(result)
    }

    @Test
    fun shouldBeAbleToReturnFalseIfConfigNameIsPresentInTheDatabase() {

        val databaseOperations = DatabaseOperations(TestConnector())
        val configName = "xyz"

        val result = databaseOperations.isConfigPresentInDatabase(configName)

        assertFalse(result)
    }

    @Test
    fun shouldBeAbleToAddTheFieldsInTheDatabase() {
        val databaseOperations = DatabaseOperations(TestConnector())
        val configName = "configuration"
        val jsonData = createJsonTemplate()
        databaseOperations.saveNewConfigurationInDatabase(configName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configName,it)
        }
        val fieldName = "Export Number"
        val result = databaseOperations.isFieldPresentInTheDatabase(fieldName)
        assertTrue(result)

    }

    @Test
    fun shouldNotBeAbleToAddTheFieldsInTheDatabase() {
        val databaseOperations = DatabaseOperations(TestConnector())
        val configurationName = "testConfiguration"
        val jsonData = createJsonTemplate()
        databaseOperations.saveNewConfigurationInDatabase(configurationName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configurationName,it)
        }
        val fieldName = "abc"
        val result = databaseOperations.isFieldPresentInTheDatabase(fieldName)
        assertFalse(result)
    }

    @Test
    fun shouldBeAbleToReadConfigDataFromDatabase() {
        val databaseOperations = DatabaseOperations(TestConnector())
        val configurationName = "testConfiguration"
        val jsonData = createJsonTemplate()
        databaseOperations.saveNewConfigurationInDatabase(configurationName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configurationName,it)
        }
        val actualConfigurationData = databaseOperations.readConfiguration(configurationName)
        val expectedFieldName = jsonData.first().fieldName

        val actualFieldName = actualConfigurationData.first().fieldName

        assertEquals(expectedFieldName , actualFieldName)
    }

    private fun createJsonTemplate(): Array<ConfigurationTemplate> {
        val metaData =
            """[{"fieldName":"Export Number","type":"Number","length":"","dependentOn":"","dependentValue":"",nullValue: "Allowed"},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export",nullValue: "Allowed","dependentValue":"N"}]"""
        return Gson().fromJson(metaData, Array<ConfigurationTemplate>::class.java)
    }
}