package validation

import jsonTemplate.JsonMetaDataTemplate
import org.json.JSONArray
import org.json.JSONObject

class LengthValidation {

    fun validateLength(jsonArrayData: JSONArray, fieldArray: Array<JsonMetaDataTemplate>): JSONArray {
        val lengthErrors = JSONArray()
        jsonArrayData.forEachIndexed { index, element ->
            val fieldElement = (element as JSONObject)
            val keys = fieldElement.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                val value = fieldElement.get(key) as String
                var flag = checkIfLengthIsIncorrect(field, value)
                if (!flag) {
                    val jsonObject = errorMessage(index, field)
                    lengthErrors.put(jsonObject)
                }
            }
        }
        return lengthErrors
    }

    private fun lengthCheck(data: String, length: Int): Boolean {
        return length == data.length
    }

    private fun checkIfLengthIsIncorrect(field: JsonMetaDataTemplate, value: String): Boolean {
        if (field.length != null && value.isNotEmpty()) {
            return (lengthCheck(value, field.length))
        }
        return true
    }

    private fun errorMessage(index: Int, field: JsonMetaDataTemplate): JSONObject {
        return JSONObject().put(
            (index + 1).toString(),
            "Incorrect length of ${field.fieldName}. Please change its length to ${field.length}"
        )
    }

}