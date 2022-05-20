package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class NullValidation : Validation {

    private val mapOfNullErrors = mutableMapOf<String , MutableList<String>>()

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): MutableMap<String, MutableList<String>> {
        val nullErrors = JSONArray()
        mapOfNullErrors.clear()
        jsonArrayData.forEachIndexed { index, element ->
            val (ele, keys) = getElementKeys(element)
            for (key in keys) {
                val (field, value) = getFieldValues(fieldArray, key, ele)
                val isNullAllowed = validateNullInEachRow(field, value)
                getErrorMessages(isNullAllowed, index, field, nullErrors)
            }
        }
        return mapOfNullErrors
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

    private fun getElementKeys(element: Any?): Pair<JSONObject, MutableSet<String>> {
        val ele = (element as JSONObject)
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
            if(mapOfNullErrors[field.fieldName] == null) {
                mapOfNullErrors[field.fieldName] = mutableListOf()
            }
            if(!mapOfNullErrors[field.fieldName]!!.contains((index+1).toString())) {
                mapOfNullErrors[field.fieldName]?.add((index+2).toString())
            }
        }
    }

}