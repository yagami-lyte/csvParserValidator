package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class TypeValidation : Validation {

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val typeErrors = JSONArray()
        val typeValidation = TypeValidation()
        jsonArrayData.forEachIndexed { index, element ->
            val (ele, keys) = getElementKeys(element)
            for (key in keys) {
                val (field, value) = getFieldValues(fieldArray, key, ele)
                var isLengthValid = validateTypeInEachRow(field, value, typeValidation)
                getErrorMessages(isLengthValid, index, field, typeErrors)
            }
        }
        return typeErrors
    }

    private fun validateTypeInEachRow(
        field: ConfigurationTemplate,
        value: String,
        typeValidation: TypeValidation
    ): Boolean {
        var isLengthValid = true
        if (field.type == "AlphaNumeric" && value.isNotEmpty() && !typeValidation.isAlphaNumeric(value)) {
            isLengthValid = false
        } else if (field.type == "Alphabet" && value.isNotEmpty() && !typeValidation.isAlphabetic(value)) {
            isLengthValid = false
        } else if (field.type == "Number" && value.isNotEmpty() && !typeValidation.isNumeric(value)) {
            isLengthValid = false
        }
        return isLengthValid
    }

    private fun getFieldValues(
        fieldArray: Array<ConfigurationTemplate>,
        key: String,
        ele: JSONObject
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
        isLengthValid: Boolean,
        index: Int,
        field: ConfigurationTemplate,
        typeErrors: JSONArray
    ) {
        if (!isLengthValid) {
            val jsonObject = JSONObject().put(
                (index + 1).toString(), "Incorrect Type of ${field.fieldName}. Please change to ${field.type}"
            )
            typeErrors.put(jsonObject)
        }
    }

    fun isNumeric(value: String): Boolean {
        return value.matches("-?\\d+(\\.\\d+)?".toRegex())
    }

    fun isAlphabetic(value: String): Boolean {
        return value.all { it.isLetter() }
    }

    fun isAlphaNumeric(value: String): Boolean {
        return value.all { it.isLetterOrDigit() }
    }
}