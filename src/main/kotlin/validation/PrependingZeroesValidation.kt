package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class PrependingZeroesValidation:Validation {

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val prePendingErrors = JSONArray()

        jsonArrayData.forEachIndexed { index, element ->
            val ele = (element as JSONObject)
            val keys = ele.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                var flag = true
                val value = ele.get(key) as String
                if (field.type == "Number" && value.isNotEmpty() && checkPrePendingZeros(value)) {
                    flag = false
                }
                if (!flag) {
                    val jsonObject = JSONObject().put(
                        (index + 1).toString(),
                        "Incorrect Type of ${field.fieldName}. Please remove PrePending Zeros!"
                    )
                    prePendingErrors.put(jsonObject)
                }
            }
        }
        return prePendingErrors
    }

    fun checkPrePendingZeros(value: String): Boolean {
        return value.startsWith("0")
    }

}
