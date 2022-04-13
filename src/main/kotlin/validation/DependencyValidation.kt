package validation

import JsonMetaDataTemplate
import org.json.JSONArray
import org.json.JSONObject

class DependencyValidation {

    fun checkDependency(jsonArrayData: JSONArray, fieldArray: Array<JsonMetaDataTemplate>): JSONArray {
        val dependencyErrors = JSONArray()
        jsonArrayData.forEachIndexed { index, element ->
            val fieldElement = (element as JSONObject)
            val keys = fieldElement.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }
                val value = fieldElement.get(key) as String
                var flag = checkIfDependencyExists(field, value)
                if (!flag) {
                    val jsonObject = errorMessage(index, field)
                    dependencyErrors.put(jsonObject)
                }
            }
        }
        return dependencyErrors
    }

    private fun checkIfDependencyExists(field: JsonMetaDataTemplate, value: String): Boolean {
        return (field.dependentOn.isNotEmpty() && field.dependentValue.isNotEmpty() && value.isEmpty())
    }

    private fun errorMessage(index: Int, field: JsonMetaDataTemplate): JSONObject {
        return JSONObject().put(
            (index + 1).toString(),
            "Value of ${field.fieldName} is dependent on ${field.dependentOn}.Do not leave ${field.fieldName} empty."
        )
    }
}