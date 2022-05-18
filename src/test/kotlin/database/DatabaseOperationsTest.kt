package database

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DatabaseOperationsTest {

    @Test
    fun shouldBeAbleToReturnTrueIfConfigNameIsPresentInTheDatabase() {

        DatabaseConnection.makeConnection()
        val databaseOperations = DatabaseOperations()
        val configName = "test4"
        databaseOperations.saveNewConfigurationInDatabase(configName)

        val result = databaseOperations.isConfigPresentInDatabase(configName)
        assertTrue(result)
    }

    @Test
    fun shouldBeAbleToReturnFalseIfConfigNameIsPresentInTheDatabase() {

        DatabaseConnection.makeConnection()
        val databaseOperations = DatabaseOperations()
        val configName = "xyz"

        val result = databaseOperations.isConfigPresentInDatabase(configName)
        assertFalse(result)
    }


}