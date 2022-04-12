package validation

import JsonMetaDataTemplate
import org.json.JSONArray
import org.json.JSONObject

class LengthValidation {

    fun validateLength(jsonArrayData: JSONArray, fieldArray:Array<JsonMetaDataTemplate>): JSONArray {
        val lengthErrors = JSONArray()
        val lengthValidation = LengthValidation()

        jsonArrayData.forEachIndexed { index, element ->
            val fieldElement = (element as JSONObject)
            val keys = fieldElement.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                val value = fieldElement.get(key) as String
                var flag = true
                if (field.length != null && value.isNotEmpty()) {
                    if (!lengthValidation.lengthCheck(value, field.length)) {
                        flag = false
                    }
                }
                if (!flag) {
                    val jsonObject = JSONObject().put(
                        (index + 1).toString(),
                        "Incorrect length of ${field.fieldName}. Please change its length to ${field.length}"
                    )
                    lengthErrors.put(jsonObject)
                }
            }
        }
        return lengthErrors
    }

    private fun lengthCheck(data: String, length: Int):Boolean{
        return length == data.length
    }



}