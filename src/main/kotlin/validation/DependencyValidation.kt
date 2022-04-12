package validation

import JsonMetaDataTemplate
import org.json.JSONArray
import org.json.JSONObject

class DependencyValidation(private val jsonArray: JSONArray, private val fieldArray: Array<JsonMetaDataTemplate>) {


    fun dependencyCheck() :JSONArray{
        val jsonArrayOfDependencyElements = JSONArray()

        jsonArray.forEachIndexed { index, element ->
            val fieldElement = (element as JSONObject)
            val keys = fieldElement.keySet()
            for (key in keys) {
                val field = fieldArray.first { it.fieldName == key }

//                println("FieldName : ${field.fieldName} ::::: ${field.dependentOn}")
                if(field.dependentOn != "" && field.dependentValue != "") {
                    val dependentField = field.dependentOn
                    val dependentFieldValue = fieldElement.get(dependentField).toString()

                    if(dependentFieldValue == field.dependentValue) {
                        val fieldValue = fieldElement.get(key).toString()
                        if(fieldValue.isNotEmpty()) {
                            val jsonObject = JSONObject().put(
                                (index+1).toString(),
                                "Value of $dependentField is $dependentFieldValue, Hence $key should be empty"
                            )

                            jsonArrayOfDependencyElements.put(jsonObject)
                        }
                    }
                }

            }
        }

        return jsonArrayOfDependencyElements
    }
}