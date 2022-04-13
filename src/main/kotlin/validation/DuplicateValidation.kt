package validation

import org.json.JSONArray
import org.json.JSONObject

class DuplicateValidation {

    fun checkDuplicates(jsonArray: JSONArray): JSONArray {
        val mapOfJsonElements: MutableMap<String, Int> = mutableMapOf()
        val jsonArrayOfDuplicateElements = JSONArray()
        jsonArray.forEachIndexed { index, element ->
            addElementToMap(mapOfJsonElements, element, index, jsonArrayOfDuplicateElements)
        }
        return jsonArrayOfDuplicateElements
    }

    private fun addElementToMap(
        mapOfJsonElements: MutableMap<String, Int>,
        element: Any, index: Int,
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
        element: Any,
        jsonArrayOfDuplicateElements: JSONArray
    ) {
        val jsonObject = JSONObject().put(
            (index + 1).toString(), "Row Duplicated From ${mapOfJsonElements[element.toString()]}"
        )
        jsonArrayOfDuplicateElements.put(jsonObject)
    }
}
