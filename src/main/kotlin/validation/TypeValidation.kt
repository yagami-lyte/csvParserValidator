package validation

import JsonMetaDataTemplate
import org.json.JSONArray
import org.json.JSONObject

class TypeValidation {

    fun typeCheck(dataInJSONArray: JSONArray , fieldArray:Array<JsonMetaDataTemplate>): JSONArray {
        val typeErrors = JSONArray()
        dataInJSONArray.forEachIndexed { index, element ->
            val ele = (element as JSONObject)
            val keys = ele.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                val value = ele.get(key) as String
                var flag = checkIfTypeIsIncorrect(field, value)
                if (!flag) {
                    val jsonObject = errorMessage(index, field)
                    typeErrors.put(jsonObject)
                }
            }
        }
        return typeErrors
    }

    private fun checkIfTypeIsIncorrect(field:JsonMetaDataTemplate, value:String):Boolean{
        if (field.type == "AlphaNumeric" && value.isNotEmpty() && isAlphaNumeric(value)) {
            return false
        } else if (field.type == "Alphabets" && value.isNotEmpty() && isAlphabetic(value)) {
            return false
        } else if (field.type == "Number" && value.isNotEmpty() && isNumeric(value)) {
            return false
        }
        return true
    }

    private fun errorMessage(index: Int, field: JsonMetaDataTemplate): JSONObject {
        return JSONObject().put(
            (index + 1).toString(), "Incorrect Type of ${field.fieldName}. Please change to ${field.type}"
        )
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