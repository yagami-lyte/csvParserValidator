package database

import jsonTemplate.ConfigurationTemplate
import java.sql.PreparedStatement
import java.sql.ResultSet

class DatabaseOperations {
    /*To run .sql files: Source<absoule path>
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
        val queryTemplate = """
            INSERT INTO csv_files (csv_name) 
            SELECT * FROM (SELECT (?)) AS temp
            WHERE NOT EXISTS (SELECT csv_name from csv_files WHERE csv_name = (?) )
            LIMIT 1; """
        val insertStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate)
        insertStatement.setString(1, csvName)
        insertStatement.setString(2, csvName)
        insertStatement.executeUpdate()
    }

    fun writeConfiguration(csvName: String, jsonData: ConfigurationTemplate) {
        val insertQueryTemplate =
            """INSERT INTO csv_fields
              (csv_name,field_name, field_type, is_null_allowed,field_length,
               dependent_field, dependent_value,date_type, time_type, datetime) 
              VALUES(?,?,?,?,?,?,?,?,?,?)"""
        val preparedInsertStatement = DatabaseConnection.makeConnection().prepareStatement(insertQueryTemplate)
        setQueryFieldsWhileSavingConfig(preparedInsertStatement, csvName, jsonData)
        preparedInsertStatement.executeUpdate()
        if(jsonData.values?.isNotEmpty() == true){
            insertAllowedValues(csvName, jsonData.fieldName, jsonData.values)
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

    private fun insertAllowedValues(csvName: String, fieldName:String, values: List<String>?) {
        values?.forEach {
            val queryTemplate = "INSERT INTO field_values(csv_name, field_name, allowed_value) VALUES(?,?,?)"
            val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate)
            preparedStatement.setString(1, csvName)
            preparedStatement.setString(2, fieldName)
            preparedStatement.setString(3, it)
            preparedStatement.executeUpdate()
        }
    }

    fun readConfiguration(csvName: String): Array<ConfigurationTemplate> {
        val query = """
            SELECT * 
            FROM csv_fields
            WHERE entry_date = (SELECT MAX(entry_date) FROM csv_fields WHERE csv_name = (?));
        """
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(query)
        preparedStatement.setString(1, csvName)
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
        val date = result.getString("date_type")
        val time = result.getString("time_type")
        val datetime = result.getString("datetime")
        val values = getValues(csvName, fieldName)
        return ConfigurationTemplate(
            csvName = csvName,
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


    private fun getValues(csvName: String, fieldName: String): List<String>? {
        val query = "SELECT allowed_value from field_values WHERE csv_name = (?) AND field_name = (?);"
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(query)
        preparedStatement.setString(1, csvName)
        preparedStatement.setString(2, fieldName)
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
