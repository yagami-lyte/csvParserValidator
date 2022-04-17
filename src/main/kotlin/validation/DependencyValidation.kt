package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class DependencyValidation : Validation {

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val dependencyErrors = JSONArray()
        jsonArrayData.forEachIndexed { index, element ->
            validateDependency(element as JSONObject, fieldArray, index, dependencyErrors)
        }
        return dependencyErrors

    }

    private fun validateDependency(
        element: JSONObject,
        fieldArray: Array<ConfigurationTemplate>,
        index: Int,
        dependencyErrors: JSONArray
    ) {
        val (fieldElement, keys) = getFieldElementKeys(element)
        for (key in keys) {
            val (field, value) = getValueKeys(fieldArray, key, fieldElement)
            var flag = true
            if (field.dependentOn.isNotEmpty()) {
                if (field.dependentValue.isNotEmpty() && value.isEmpty()) {
                    flag = false
                }
            }
            getErrorMessages(flag, index, field, dependencyErrors)
        }
    }

    private fun getValueKeys(fieldArray: Array<ConfigurationTemplate>, key: String, fieldElement: JSONObject): Pair<ConfigurationTemplate, String> {
        val field = fieldArray.first { it.fieldName == key }
        val value = fieldElement.get(key) as String
        return Pair(field, value)
    }

    private fun getFieldElementKeys(element: Any?): Pair<JSONObject, MutableSet<String>> {
        val fieldElement = (element as JSONObject)
        val keys = fieldElement.keySet()
        return Pair(fieldElement, keys)
    }

    private fun getErrorMessages(
        flag: Boolean,
        index: Int,
        field: ConfigurationTemplate,
        dependencyErrors: JSONArray
    ) {
        if (!flag) {
            val jsonObject = JSONObject().put(
                (index + 1).toString(),
                "Value of ${field.fieldName} is dependent on ${field.dependentOn}.Do not leave ${field.fieldName} empty."
            )
            dependencyErrors.put(jsonObject)
        }
    }
}