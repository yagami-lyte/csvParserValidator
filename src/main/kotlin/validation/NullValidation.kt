package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class NullValidation : Validation {
    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val nullErrors = JSONArray()
        jsonArrayData.forEachIndexed { index, element ->
            val (ele, keys) = getElementKeys(JSONObject(element))
            for (key in keys) {
                val (field, value) = getFieldValues(fieldArray, key, ele)
                val isNullAllowed = validateNullInEachRow(field, value)
                getErrorMessages(isNullAllowed, index, field, nullErrors)
            }
        }
        return nullErrors
    }

    private fun validateNullInEachRow(
        field: ConfigurationTemplate,
        value: String,
    ): Boolean {
        var isNullAllowed = true
        if (field.nullValue == "Allowed" && value.isEmpty()) {
            isNullAllowed = true
        }
        else if (field.nullValue == "Not Allowed" && value.isEmpty())  {
            isNullAllowed = false
        }
        return isNullAllowed
    }

    private fun getFieldValues(
        fieldArray: Array<ConfigurationTemplate>,
        key: String,
        ele: JSONObject,
    ): Pair<ConfigurationTemplate, String> {
        val field = fieldArray.first { it.fieldName == key }
        val value = ele.get(key) as String
        return Pair(field, value)
    }

    private fun getElementKeys(element: JSONObject): Pair<JSONObject, MutableSet<String>> {
        val ele = JSONObject(element)
        val keys = ele.keySet()
        return Pair(ele, keys)
    }

    private fun getErrorMessages(
        isNullAllowed: Boolean,
        index: Int,
        field: ConfigurationTemplate,
        nullErrors: JSONArray,
    ) {
        if (!isNullAllowed) {
            var errorMsg = "Has empty value for ${field.fieldName}. Please enter a value in your CSV."
            val jsonObject = JSONObject().put(
                (index + 1).toString(), errorMsg
            )
            nullErrors.put(jsonObject)
        }
    }

}