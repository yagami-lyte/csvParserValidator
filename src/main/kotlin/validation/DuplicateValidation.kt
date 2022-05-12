package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class DuplicateValidation : Validation {

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val mapOfJsonElements: MutableMap<String, Int> = mutableMapOf()
        val jsonArrayOfDuplicateElements = JSONArray()
        jsonArrayData.forEachIndexed { index, element ->
            addElementToMap(mapOfJsonElements, JSONObject(element), index, jsonArrayOfDuplicateElements)
        }
        return jsonArrayOfDuplicateElements
    }

    private fun addElementToMap(
        mapOfJsonElements: MutableMap<String, Int>,
        element: JSONObject,
        index: Int,
        jsonArrayOfDuplicateElements: JSONArray
    ) {
        if (mapOfJsonElements[element.toString()] == null) {
            mapOfJsonElements[element.toString()] = index + 1
            return
        }

        getJsonObject(index, mapOfJsonElements, element, jsonArrayOfDuplicateElements)
    }

    private fun getJsonObject(
        index: Int,
        mapOfJsonElements: MutableMap<String, Int>,
        element: JSONObject,
        jsonArrayOfDuplicateElements: JSONArray
    ) {
        val jsonObject = JSONObject().put(
            (index + 1).toString(), "Row Duplicated From ${mapOfJsonElements[element.toString()]}"
        )
        jsonArrayOfDuplicateElements.put(jsonObject)
    }
}
