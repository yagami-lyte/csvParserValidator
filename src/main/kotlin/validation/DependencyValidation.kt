package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class DependencyValidation : Validation {

    private val mapOfDependencyErrors = mutableMapOf<String, MutableList<String>>()

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): MutableMap<String, MutableList<String>> {
        mapOfDependencyErrors.clear()
        jsonArrayData.forEachIndexed { index, element ->
            validateDependency(element as JSONObject, fieldArray, index)
        }
        return mapOfDependencyErrors
    }

    private fun validateDependency(element: JSONObject, fieldArray: Array<ConfigurationTemplate>, index: Int) {
        val (fieldElement, keys) = getFieldElementKeys(element)
        for (key in keys) {
            val (field, value) = getValueKeys(fieldArray, key, fieldElement)
            var flag = true
            if (field.dependentOn.isNotEmpty() && (field.dependentValue.isNotEmpty() && value.isEmpty())) {
                flag = false
            }
            getErrorMessages(flag, index, field)
        }
    }

    private fun getValueKeys(fieldArray: Array<ConfigurationTemplate>, key: String, fieldElement: JSONObject): Pair<ConfigurationTemplate, String> {
        val field = fieldArray.first { it.fieldName == key }
        val value = fieldElement.get(key) as String
        return Pair(field, value)
    }

    private fun getFieldElementKeys(element: JSONObject): Pair<JSONObject, MutableSet<String>> {
        val keys = element.keySet()
        return Pair(element, keys)
    }

    private fun getErrorMessages(flag: Boolean, index: Int, field: ConfigurationTemplate) {
        if (!flag) {
            when(mapOfDependencyErrors[field.fieldName]) {null -> mapOfDependencyErrors[field.fieldName] = mutableListOf()
            }
            mapOfDependencyErrors[field.fieldName]?.add((index + 2).toString())
        }
    }
}