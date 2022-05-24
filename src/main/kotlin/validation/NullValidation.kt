package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class NullValidation : Validation {

    private val mapOfNullErrors = mutableMapOf<String, MutableList<String>>()

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): MutableMap<String, MutableList<String>> {

        mapOfNullErrors.clear()
        jsonArrayData.forEachIndexed { index, element ->
            val (ele, keys) = getElementKeys(element as JSONObject)
            for (key in keys) {
                val (field, value) = getFieldValues(fieldArray, key, ele)
                val isNullAllowed = validateNullInEachRow(field, value)
                getErrorMessages(isNullAllowed, index, field)
            }
        }
        return mapOfNullErrors
    }

    private fun validateNullInEachRow(field: ConfigurationTemplate, value: String): Boolean {
        var isNullAllowed = true
        if(value.isEmpty()) {
            when (field.nullValue) {
                "Allowed" -> isNullAllowed = true
                "Not Allowed" -> isNullAllowed = false
            }
        }
        return isNullAllowed
    }

    private fun getFieldValues(fieldArray: Array<ConfigurationTemplate>, key: String, ele: JSONObject): Pair<ConfigurationTemplate, String> {
        val field = fieldArray.first { it.fieldName == key }
        val value = ele.get(key) as String
        return Pair(field, value)
    }

    private fun getElementKeys(element: JSONObject): Pair<JSONObject, MutableSet<String>> {
        val keys = element.keySet()
        return Pair(element, keys)
    }

    private fun getErrorMessages(
        isNullAllowed: Boolean,
        index: Int,
        field: ConfigurationTemplate
    ) {
        if (!isNullAllowed) {
            when (mapOfNullErrors[field.fieldName]) {
                null -> mapOfNullErrors[field.fieldName] = mutableListOf()
            }
            if (!mapOfNullErrors[field.fieldName]!!.contains((index + 1).toString())) {
                mapOfNullErrors[field.fieldName]?.add((index + 2).toString())
            }
        }
    }
}