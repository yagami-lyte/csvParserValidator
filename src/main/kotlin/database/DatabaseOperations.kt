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
    fun saveNewConfigurationInDatabase(configurationName: String){
        val queryTemplate = "INSERT INTO configuration(config_name) VALUES('$configurationName');"
        val insertStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate ,ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        insertStatement.executeUpdate()
        //has not checked the configname already exist
    }

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

    private fun getConfigurationId(configurationName: String): Int {
        val queryTemplate = "SELECT config_id FROM configuration WHERE config_name = '$configurationName';"
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate, ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        val result = preparedStatement.executeQuery()
        result.first()
        return result.getInt("config_id")
    }

    fun getConfigNames(): List<String>? {
        val queryTemplate = "SELECT config_name FROM configuration ;"
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate, ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        val result = preparedStatement.executeQuery()
        val values = mutableListOf<String>()
        while (result.next()) {
            values.add(result.getString("config_name"))
        }
        if (values.isEmpty()) {
            return null
        }
        return values
    }


    fun writeConfiguration(configurationName: String, jsonData: ConfigurationTemplate) {
        val configId = getConfigurationId(configurationName)
        val insertQueryTemplate =
            """INSERT INTO csv_fields
              (config_id, field_name, field_type, is_null_allowed, field_length,
               dependent_field, dependent_value,date_type, time_type, datetime_type) 
              VALUES($configId,?,?,?,?,?,?,?,?,?)"""
        val preparedInsertStatement = DatabaseConnection.makeConnection().prepareStatement(insertQueryTemplate, ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        setQueryFieldsWhileSavingConfig(preparedInsertStatement,  jsonData)
        preparedInsertStatement.executeUpdate()
        if(jsonData.values?.isNotEmpty() == true){
            val fieldId = getFieldId(configId)
            insertAllowedValues(fieldId, jsonData.values)
        }
    }

    private fun getFieldId(configId: Int): Int {
        val queryTemplate = "SELECT field_id FROM csv_fields WHERE config_id = $configId;"
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate, ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        val result = preparedStatement.executeQuery()
        result.first()
        return result.getInt("field_id")
    }

    private fun setQueryFieldsWhileSavingConfig(
        preparedStatement: PreparedStatement,
        jsonData: ConfigurationTemplate
    ) {
        preparedStatement.setString(1, jsonData.fieldName)
        preparedStatement.setString(2, jsonData.type)
        preparedStatement.setString(3, jsonData.nullValue)
        preparedStatement.setString(4, jsonData.length)
        preparedStatement.setString(5, jsonData.dependentOn)
        preparedStatement.setString(6, jsonData.dependentValue)
        preparedStatement.setString(7, jsonData.date)
        preparedStatement.setString(8, jsonData.time)
        preparedStatement.setString(9, jsonData.datetime)
    }

    private fun insertAllowedValues(fieldId: Int,  values: List<String>?) {
        values?.forEach {
            val queryTemplate = "INSERT INTO field_values( allowed_value , field_id) VALUES(?,$fieldId)"
            val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(queryTemplate, ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE)
            preparedStatement.setString(1, it)
            preparedStatement.executeUpdate()
        }
    }

    fun readConfiguration(configName: String): Array<ConfigurationTemplate> {
        val configId = getConfigurationId(configName)
        val query = """
            SELECT * 
            FROM csv_fields
            WHERE config_id = ($configId);
        """
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
        val result = preparedStatement.executeQuery()
        val finalConfig = mutableListOf<ConfigurationTemplate>()
        while (result.next()) {
            finalConfig.add(getJsonConfig(result, configName))
        }
        return finalConfig.toTypedArray()
    }


    private fun getJsonConfig(result: ResultSet, configName: String): ConfigurationTemplate{
        val fieldName = result.getString("field_name")
        val fieldType = result.getString("field_type")
        val isNullAllowed = result.getString("is_null_allowed")
        val fieldLength = result.getString("field_length")
        val dependentField = result.getString("dependent_field")
        val dependentValue = result.getString("dependent_value")
        val date = result.getString("date_type")
        val time = result.getString("time_type")
        val datetime = result.getString("datetime_type")
        val values = getValues(result.getInt("field_id"))
        return ConfigurationTemplate(
            configName = configName,
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


    private fun getValues(field_id: Int): List<String>? {
        val query = "SELECT allowed_value FROM field_values WHERE field_id = ($field_id)"
        val preparedStatement = DatabaseConnection.makeConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_UPDATABLE)
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
