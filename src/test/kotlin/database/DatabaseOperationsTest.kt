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
        val configName = "configuration"
        val jsonData = createJsonTemplate()
        databaseOperations.saveNewConfigurationInDatabase(configName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configName,it)
        }
        val fieldName = "abc"
        val result = databaseOperations.isFieldPresentInTheDatabase(fieldName)
        assertFalse(result)

    }

    private fun createJsonTemplate(): Array<ConfigurationTemplate> {
        val metaData =
            """[{"fieldName":"Export Number","type":"Number","length":"","dependentOn":"","dependentValue":"","values":[]},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export","dependentValue":"N","values":["Export,Country Name","Y,","N,USA",""]}]"""
        return Gson().fromJson(metaData, Array<ConfigurationTemplate>::class.java)
    }


}