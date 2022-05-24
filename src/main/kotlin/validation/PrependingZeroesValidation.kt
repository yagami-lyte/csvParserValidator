package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class PrependingZeroesValidation : Validation {

    private val mapOfPrePendingErrors = mutableMapOf<String, MutableList<String>>()

    override fun validate(
        jsonArrayData: JSONArray,
        fieldArray: Array<ConfigurationTemplate>,
    ): MutableMap<String, MutableList<String>> {

        mapOfPrePendingErrors.clear()
        jsonArrayData.forEachIndexed { index, element ->
            if (validatePrependingZeroFields(element as JSONObject, fieldArray, index)) return mapOfPrePendingErrors
        }
        return mapOfPrePendingErrors
    }

    private fun validatePrependingZeroFields(
        element: JSONObject,
        fieldArray: Array<ConfigurationTemplate>,
        index: Int,
    ): Boolean {
        val keys = element.keySet()
        for (key in keys) {
            val field = fieldArray.first { it.fieldName == key }
            var flag = true
            val value = element.get(key) as String
            if (field.type == "Number" && value.isNotEmpty() && checkPrePendingZeros(value)) {
                flag = false
            }
            if (getErrorMessages(flag, field, index)) return true
        }
        return false
    }

    private fun getErrorMessages(flag: Boolean, field: ConfigurationTemplate, index: Int): Boolean {
        if (!flag) {
            when (mapOfPrePendingErrors[field.fieldName]) {
                null -> mapOfPrePendingErrors[field.fieldName] = mutableListOf()
            }
            mapOfPrePendingErrors[field.fieldName]?.add((index + 2).toString())
            return true
        }
        return false
    }

    fun checkPrePendingZeros(value: String): Boolean {
        return value.startsWith("0")
    }

}
