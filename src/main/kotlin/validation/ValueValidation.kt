package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class ValueValidation : Validation {

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val valueErrors = JSONArray()
        jsonArrayData.forEachIndexed { index, element ->
            valueValidationForEachRow(element as JSONObject, fieldArray, index, valueErrors)
        }
        return valueErrors
    }

    private fun valueValidationForEachRow(
        element: Any?,
        fieldArray: Array<ConfigurationTemplate>,
        index: Int,
        valueErrors: JSONArray
    ) {
        val (ele, keys) = getElementKeys(element)
        for (key in keys) {
            val (field, value) = getFieldValues(fieldArray, key, ele)
            val flag = checkIfValueIsIncorrect(field, value)
            getErrorMessages(flag, index, field, valueErrors)
        }
    }

    private fun getElementKeys(element: Any?): Pair<JSONObject, MutableSet<String>> {
        val ele = (element as JSONObject)
        val keys = ele.keySet()
        return Pair(ele, keys)
    }

    private fun getFieldValues(fieldArray: Array<ConfigurationTemplate>, key: String?, ele: JSONObject): Pair<ConfigurationTemplate, String> {
        val field = fieldArray.first { it.fieldName == key }
        val value = ele.get(key) as String
        return Pair(field, value)
    }

    private fun getErrorMessages(flag: Boolean, index: Int, field: ConfigurationTemplate, valueErrors: JSONArray) {
        if (!flag) {
            val jsonObject = errorMessage(index, field)
            valueErrors.put(jsonObject)
        }
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
            "Incorrect Value of ${field.fieldName}. Please select value from ${field.values} in the CSV."
        )
    }
}