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
        val jsonData = createJsonTemplateWithDependencyFields()
        databaseOperations.saveNewConfigurationInDatabase(configName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configName,it)
        }
        val fieldName = "Export Number"

        val actualResponse = databaseOperations.isFieldPresentInTheDatabase(fieldName)

        assertTrue(actualResponse)
    }

    @Test
    fun shouldNotBeAbleToAddTheFieldsInTheDatabase() {
        val databaseOperations = DatabaseOperations(TestConnector())
        val configurationName = "testConfiguration"
        val jsonData = createJsonTemplateWithDependencyFields()
        databaseOperations.saveNewConfigurationInDatabase(configurationName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configurationName,it)
        }
        val fieldName = "abc"

        val actualResponse = databaseOperations.isFieldPresentInTheDatabase(fieldName)

        assertFalse(actualResponse)
    }

    @Test
    fun shouldBeAbleToReadConfigDataFromDatabase() {
        val databaseOperations = DatabaseOperations(TestConnector())
        val configurationName = "testConfiguration"
        val jsonData = createJsonTemplateWithDependencyFields()
        databaseOperations.saveNewConfigurationInDatabase(configurationName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configurationName,it)
        }
        val actualConfigurationData = databaseOperations.readConfiguration(configurationName)
        val expectedFieldName = jsonData.first().fieldName

        val actualFieldName = actualConfigurationData.first().fieldName

        assertEquals(expectedFieldName , actualFieldName)
    }

    @Test
    fun shouldBeAbleToGetConfigNamesFromTheDatabase() {

        val databaseOperations = DatabaseOperations(TestConnector())
        val configName1 = "Config1"
        val configName2 = "Config2"
        databaseOperations.saveNewConfigurationInDatabase(configName1)
        databaseOperations.saveNewConfigurationInDatabase(configName2)
        val expectedConfigNames = "[Config1, Config2]"

        val actualConfigNames = databaseOperations.getConfigNames().toString()

        assertEquals(expectedConfigNames , actualConfigNames)
    }


    @Test
    fun shouldBeAbleToAddTheDateTimeFieldsInTheDatabase() {
        val databaseOperations = DatabaseOperations(TestConnector())
        val configurationName = "testConfigurationForDateTimeFormat"
        val jsonData = createJsonTemplateWithDateTimeFormats()
        databaseOperations.saveNewConfigurationInDatabase(configurationName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configurationName,it)
        }

        val actualResponse = databaseOperations.isConfigPresentInDatabase(configurationName)

        assertTrue(actualResponse)
    }

    @Test
    fun shouldBeAbleToAddTheLengthFieldInTheDatabase() {
        val databaseOperations = DatabaseOperations(TestConnector())
        val configName = "testConfigurationWithLengthFields"
        val jsonData = createJsonTemplateWithLengthField()
        databaseOperations.saveNewConfigurationInDatabase(configName)
        jsonData.forEach {
            databaseOperations.writeConfiguration(configName,it)
        }

        val actualResponse = databaseOperations.isConfigPresentInDatabase(configName)

        assertTrue(actualResponse)
    }

    private fun createJsonTemplateWithDependencyFields(): Array<ConfigurationTemplate> {
        val metaData =
            """[{"fieldName":"Export Number","type":"Number","length":"","dependentOn":"","dependentValue":"",nullValue: "Allowed"},{"fieldName":"Country Name","type":"Alphabets","length":"4","dependentOn":"Export",nullValue: "Allowed","dependentValue":"N"}]"""
        return Gson().fromJson(metaData, Array<ConfigurationTemplate>::class.java)
    }

    private fun createJsonTemplateWithDateTimeFormats(): Array<ConfigurationTemplate> {
        val metaData =
            """[{"configName":"configuration","datetime":"MMMM dd, yy","date":"","time":"","nullValue":"Not Allowed","fieldName":"operation","type":"Date Time","length":"","dependentOn":"","dependentValue":""},{"configName":"","datetime":"","date":"","time":"","nullValue":"Not Allowed","fieldName":"requestedAt","type":"Alphabets","length":"","dependentOn":"","dependentValue":""}]"""
        return Gson().fromJson(metaData, Array<ConfigurationTemplate>::class.java)
    }

    private fun createJsonTemplateWithLengthField(): Array<ConfigurationTemplate> {
        val metaData =
            """[{"configName":"testConfigurationWithLengthFields","datetime":"","date":"","time":"","nullValue":"Not Allowed","fieldName":"Requested At","type":"Alphabets","length":"12","dependentOn":"","dependentValue":""},{"configName":"testConfigurationWithLengthFields","datetime":"","date":"","time":"","nullValue":"Not Allowed","fieldName":"requestedAt","type":"Special Characters","length":"5","dependentOn":"","dependentValue":""},{"configName":"testConfigurationWithLengthFields","datetime":"","date":"","time":"","nullValue":"Not Allowed","fieldName":"completedAt","type":"Alphabets","length":"12","dependentOn":"","dependentValue":""}]"""
        return Gson().fromJson(metaData, Array<ConfigurationTemplate>::class.java)
    }
}