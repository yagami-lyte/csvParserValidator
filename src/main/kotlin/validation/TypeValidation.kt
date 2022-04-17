package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class TypeValidation : Validation {

    override fun validate(dataInJSONArray: JSONArray , fieldArray:Array<ConfigurationTemplate>): JSONArray {
        val typeErrors = JSONArray()
        val typeValidation = TypeValidation()

        dataInJSONArray.forEachIndexed { index, element ->
            val ele = (element as JSONObject)
            val keys = ele.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                var flag = true
                val value = ele.get(key) as String
                if (field.type == "AlphaNumeric" && value.isNotEmpty() && !typeValidation.isAlphaNumeric(value)) {
                    flag = false
                } else if (field.type == "Alphabet" && value.isNotEmpty() && !typeValidation.isAlphabetic(value)) {
                    flag = false
                } else if (field.type == "Number" && value.isNotEmpty() && !typeValidation.isNumeric(value)) {
                    flag = false
                }
                if (!flag) {
                    val jsonObject = JSONObject().put(
                        (index + 1).toString(), "Incorrect Type of ${field.fieldName}. Please change to ${field.type}"
                    )
                    typeErrors.put(jsonObject)
                }
            }
        }
        return typeErrors
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