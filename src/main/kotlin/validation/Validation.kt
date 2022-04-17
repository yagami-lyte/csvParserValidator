package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray

interface Validation {
    fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray
}

