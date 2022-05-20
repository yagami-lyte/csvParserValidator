package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class PrependingZeroesValidation:Validation {

    private val mapOfPrePendingErrors = mutableMapOf<String , MutableList<String>>()

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): MutableMap<String, MutableList<String>> {

        mapOfPrePendingErrors.clear()
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
                    if (mapOfPrePendingErrors[field.fieldName] == null) {
                        mapOfPrePendingErrors[field.fieldName] = mutableListOf()
                    }
                    mapOfPrePendingErrors[field.fieldName]?.add((index + 1).toString())
                    return mapOfPrePendingErrors
                }
            }
        }
        return mapOfPrePendingErrors
    }

    fun checkPrePendingZeros(value: String): Boolean {
        return value.startsWith("0")
    }

}
