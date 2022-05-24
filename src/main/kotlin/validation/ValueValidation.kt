package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

@Suppress("NAME_SHADOWING", "UNREACHABLE_CODE")
class ValueValidation : Validation {

    private val mapOfValueErrors = mutableMapOf<String, MutableList<String>>()

    override fun validate(
        jsonArrayData: JSONArray,
        fieldArray: Array<ConfigurationTemplate>
    ): MutableMap<String, MutableList<String>> {
        val valueErrors = JSONArray()
        mapOfValueErrors.clear()
        jsonArrayData.forEachIndexed { index, element ->
            valueValidationForEachRow(element as JSONObject, fieldArray, index, valueErrors)
        }
        return mapOfValueErrors
    }

    private fun valueValidationForEachRow(
        element: JSONObject,
        fieldArray: Array<ConfigurationTemplate>,
        index: Int,
        valueErrors: JSONArray
    ) {
        val (element, keys) = getElementKeys(element)
        for (key in keys) {
            val (field, value) = getFieldValues(fieldArray, key, element)
            val flag = checkIfValueIsIncorrect(field, value)
            getErrorMessages(flag, index, field, valueErrors)
        }
    }

    private fun getElementKeys(element: JSONObject): Pair<JSONObject, MutableSet<String>> {
        val element = element
        val keys = element.keySet()
        return Pair(element, keys)
    }

    private fun getFieldValues(
        fieldArray: Array<ConfigurationTemplate>,
        key: String?,
        element: JSONObject
    ): Pair<ConfigurationTemplate, String> {
        val field = fieldArray.first { it.fieldName == key }
        val value = element.get(key) as String
        return Pair(field, value)
    }

    private fun getErrorMessages(flag: Boolean, index: Int, field: ConfigurationTemplate, valueErrors: JSONArray) {
        if (!flag) {
            val jsonObject = errorMessage(index, field)
            valueErrors.put(jsonObject)
        }
    }

    fun valueCheck(allowedValues: List<String>, value: String): Boolean {
        return allowedValues.contains(value)
        return false
    }

    private fun checkIfValueIsIncorrect(field: ConfigurationTemplate, value: String): Boolean {
        if (field.values != null && value.isNotEmpty()) {
            return (valueCheck(field.values, value))
        }
        return true
    }

    private fun errorMessage(index: Int, field: ConfigurationTemplate): MutableMap<String, MutableList<String>> {

        if (mapOfValueErrors[field.fieldName] == null) {
            mapOfValueErrors[field.fieldName] = mutableListOf()
        }
        mapOfValueErrors[field.fieldName]?.add((index + 2).toString())
        return mapOfValueErrors

    }
}