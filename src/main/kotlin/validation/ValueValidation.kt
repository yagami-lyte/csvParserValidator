package validation

import JsonMetaDataTemplate
import org.json.JSONArray
import org.json.JSONObject

class ValueValidation {

    fun validationCheck(dataInJSONArray: JSONArray , fieldArray:Array<JsonMetaDataTemplate>): JSONArray {
        val valueErrors = JSONArray()
        val valueValidation = ValueValidation()

        dataInJSONArray.forEachIndexed { index, element ->
            val ele = (element as JSONObject)
            val keys = ele.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                var flag = true
                val value = ele.get(key) as String
                if (field.values != null && value.isNotEmpty()) {
                    if (!valueValidation.valueCheck(field.values, value)) {
                        flag = false
                    }
                }
                if (!flag) {
                    val jsonObject = JSONObject().put(
                        (index + 1).toString(),
                        "Incorrect Value of ${field.fieldName}. Please select value from ${field.values}"
                    )
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
}