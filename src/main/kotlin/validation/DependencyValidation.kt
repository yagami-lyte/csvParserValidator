package validation

import jsonTemplate.JsonMetaDataTemplate
import org.json.JSONArray
import org.json.JSONObject

class DependencyValidation {

    fun checkDependency(jsonArrayData: JSONArray, fieldArray: Array<JsonMetaDataTemplate>): Any {
        val dependencyErrors = JSONArray()

        jsonArrayData.forEachIndexed { index, element ->
            val fieldElement = (element as JSONObject)
            val keys = fieldElement.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                val value = fieldElement.get(key) as String
                var flag = true
                if (field.dependentOn.isNotEmpty()) {
                    if (field.dependentValue.isNotEmpty() && value.isEmpty()) {
                        flag = false
                    }
                }
                if (!flag) {
                    val jsonObject = JSONObject().put(
                        (index + 1).toString(),
                        "Value of ${field.fieldName} is dependent on ${field.dependentOn}.Do not leave ${field.fieldName} empty."
                    )
                    dependencyErrors.put(jsonObject)
                }
            }
        }
        return dependencyErrors

    }
}