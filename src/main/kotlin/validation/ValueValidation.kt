package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class ValueValidation {

    fun validationCheck(dataInJSONArray: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val valueErrors = JSONArray()
        dataInJSONArray.forEachIndexed { index, element ->
            val ele = (element as JSONObject)
            val keys = ele.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                val value = ele.get(key) as String
                var flag = checkIfValueIsIncorrect(field, value)
                if (!flag) {
                    val jsonObject = errorMessage(index, field)
                    valueErrors.put(jsonObject)
                }
            }
        }
        return valueErrors
    }

    fun valueCheck(allowedValues: List<String>?, value: String): Boolean {
        if (allowedValues != null) {
            return allowedValues.contains(value)
        }
        return false
    }

    private fun checkIfValueIsIncorrect(field: ConfigurationTemplate, value: String): Boolean {
        if (field.values != null && value.isNotEmpty()) {
            return (valueCheck(field.values, value))
        }
        return true
    }

    private fun errorMessage(index: Int, field: ConfigurationTemplate): JSONObject {
        return JSONObject().put(
            (index + 1).toString(),
            "Incorrect Value of ${field.fieldName}. Please select value from ${field.values}"
        )
    }
}