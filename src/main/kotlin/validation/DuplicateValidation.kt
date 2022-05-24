package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class DuplicateValidation : Validation {

    private val mapOfDuplicationErrors = mutableMapOf<String, MutableList<String>>()

    override fun validate(
        jsonArrayData: JSONArray,
        fieldArray: Array<ConfigurationTemplate>
    ): MutableMap<String, MutableList<String>> {
        val mapOfJsonElements: MutableMap<String, Int> = mutableMapOf()
        mapOfDuplicationErrors.clear()
        jsonArrayData.forEachIndexed { index, element ->
            addElementToMap(mapOfJsonElements, element as JSONObject, index)
        }
        return mapOfDuplicationErrors
    }

    private fun addElementToMap(
        mapOfJsonElements: MutableMap<String, Int>,
        element: JSONObject,
        index: Int
    ) {
        if (mapOfJsonElements[element.toString()] == null) {
            mapOfJsonElements[element.toString()] = index + 1
            return
        }

        getJsonObject(index, mapOfJsonElements, element)
    }

    private fun getJsonObject(
        index: Int,
        mapOfJsonElements: MutableMap<String, Int>,
        element: JSONObject
    ) {
        val duplicatedRowNumber = (index + 1).toString()
        val duplicationRowNumber = mapOfJsonElements[element.toString()]
        when (mapOfDuplicationErrors[(index + 1).toString()]) {
            null -> mapOfDuplicationErrors[duplicatedRowNumber] = mutableListOf()
        }
        if (duplicationRowNumber != null) {
            mapOfDuplicationErrors[(index + 1).toString()]?.add(duplicationRowNumber.toString())
        }
    }
}
