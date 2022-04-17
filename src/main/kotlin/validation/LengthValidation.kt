package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class LengthValidation {

    fun validateLength(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val lengthErrors = JSONArray()
        jsonArrayData.forEachIndexed { index, element ->
            val fieldElement = (element as JSONObject)
            val keys = fieldElement.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                val value = fieldElement.get(key) as String

                var flag = true
                if(field.length != "") {
                    val fieldLength = Integer.parseInt(field.length)
                    flag = checkIfLengthIsIncorrect(fieldLength, value)
                }

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

    private fun checkIfLengthIsIncorrect(fieldLength: Int, value: String): Boolean {
        if (value != "") {
            return (lengthCheck(value, fieldLength))
        }
        return true
    }

    private fun errorMessage(index: Int, field: ConfigurationTemplate): JSONObject {
        return JSONObject().put(
            (index + 1).toString(),
            "Incorrect length of ${field.fieldName}. Please change its length to ${field.length}"
        )
    }

}