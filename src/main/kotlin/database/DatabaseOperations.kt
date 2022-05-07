package database

import jsonTemplate.ConfigurationTemplate
import java.sql.PreparedStatement
import java.sql.ResultSet

class DatabaseOperations {
    /*
    -- upload csv--
    1. should enter csv name in table: csv_file.
    2. create a new table with csv file's name if doesn't exist.
    -- set configuration --
    1. write config details into config table for that specific file
    2. retrieve latest config details if file is uploaded again
    3. should show configuration if previously added on UI for same file
    -- view errors --
    1. read from config table the configurations
    2. send to backend for parsing
     */

    fun saveNewCSVInDatabase(csvName: String){
        val queryTemplate = "INSERT INTO csv_files VALUES(?)"
        val insertStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate)
        insertStatement.setString(1, csvName)
        insertStatement.executeUpdate()
    }

    fun createConfigTableForCSVFile(csvName: String){
        val query = """
            CREATE TABLE IF NOT EXISTS (?)(
            entry_id      SERIAL PRIMARY KEY,
            field_name    VARCHAR(255) NOT NULL,
            field_type    VARCHAR(255) NOT NULL,
            is_null_allowed VARCHAR(255),
            field_length  INT,
            field_values  VARCHAR(255),
            dependent_field VARCHAR(255),
            dependent_value VARCHAR(255)),
            date VARCHAR(255)),
            time VARCHAR(255)),
            datetime VARCHAR(255)),
        """
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(query)
        preparedStatement.setString(1, csvName)
        preparedStatement.executeQuery()
    }

    private fun writeConfiguration(csvName: String, jsonData: ConfigurationTemplate) {
        val insertQueryTemplate =
            "INSERT INTO (?)(field_name, field_type, is_null_allowed,field_length, dependent_field, dependent_value, date, time, datetime) VALUES(?,?,?,?,?,?,?,?,?)"
        val preparedInsertStatement = DatabaseConnection.makeConnection().prepareStatement(insertQueryTemplate)
        setQueryFieldsWhileSavingConfig(preparedInsertStatement, csvName, jsonData)
        preparedInsertStatement.executeUpdate()
        if(jsonData.values?.isNotEmpty() == true){
            insertAllowedValues(csvName, jsonData.values)
        }
    }

    private fun setQueryFieldsWhileSavingConfig(
        preparedStatement: PreparedStatement,
        csvName: String,
        jsonData: ConfigurationTemplate
    ) {
        preparedStatement.setString(1, csvName)
        preparedStatement.setString(2, jsonData.fieldName)
        preparedStatement.setString(3, jsonData.type)
        preparedStatement.setString(4, jsonData.nullValue)
        preparedStatement.setString(5, jsonData.length)
        preparedStatement.setString(6, jsonData.dependentOn)
        preparedStatement.setString(7, jsonData.dependentValue)
        preparedStatement.setString(8, jsonData.date)
        preparedStatement.setString(9, jsonData.time)
        preparedStatement.setString(10, jsonData.datetime)
    }

    private fun insertAllowedValues(csvName: String, values: List<String>?) {
        values?.forEach {
            val queryTemplate = "INSERT INTO field_values(csvName, allowed_value) VALUES(?,?)"
            val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate)
            preparedStatement.setString(1, csvName)
            preparedStatement.setString(2, it)
            preparedStatement.executeUpdate()
        }
    }

    fun readConfiguration(csvName: String): Array<ConfigurationTemplate> {
        val query = """
            SELECT field_name,
                   field_type,
                   is_null_allowed,
                   field_length,
                   dependent_field,
                   dependent_value,
                   date,
                   time,
                   datetime
            FROM (?)
            WHERE entry_id = (SELECT MAX(entry_id) FROM (?))
        """
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(query)
        preparedStatement.setString(1, csvName)
        preparedStatement.setString(2, csvName)
        val result = preparedStatement.executeQuery()
        val finalConfig = mutableListOf<ConfigurationTemplate>()
        while (result.next()) {
            finalConfig.add(getJsonConfig(result, csvName))
        }
        return finalConfig.toTypedArray()
    }


    private fun getJsonConfig(result: ResultSet, csvName: String): ConfigurationTemplate{
        val fieldName = result.getString("field_name")
        val fieldType = result.getString("field_type")
        val isNullAllowed = result.getString("is_null_allowed")
        val fieldLength = result.getString("field_length")
        val dependentField = result.getString("dependent_field")
        val dependentValue = result.getString("dependent_value")
        val date = result.getString("date")
        val time = result.getString("time")
        val datetime = result.getString("datetime")
        val values = getValues(csvName)
        return ConfigurationTemplate(
            fieldName = fieldName,
            type = fieldType,
            nullValue = isNullAllowed,
            length = fieldLength,
            values = values,
            dependentOn = dependentField,
            dependentValue = dependentValue,
            date = date,
            time = time,
            datetime = datetime
        )
    }


    private fun getValues(csvName: String): List<String>? {
        val query = "SELECT allowed_value from field_values WHERE csv_name = ?"
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(query)
        preparedStatement.setString(1, csvName)
        val result = preparedStatement.executeQuery()
        val values = mutableListOf<String>()
        while (result.next()) {
            values.add(result.getString("allowed_value"))
        }
        if (values.isEmpty()) {
            return null
        }
        return values
    }

}
