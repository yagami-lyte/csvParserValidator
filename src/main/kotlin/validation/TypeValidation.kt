package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat


class TypeValidation : Validation {

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val typeErrors = JSONArray()
        jsonArrayData.forEachIndexed { index, element ->
            val (ele, keys) = getElementKeys(element)
            for (key in keys) {
                val (field, value) = getFieldValues(fieldArray, key, ele)
                val isLengthValid = validateTypeInEachRow(field, value)
                getErrorMessages(isLengthValid, index, field, typeErrors)
            }
        }
        return typeErrors
    }

    private fun validateTypeInEachRow(
        field: ConfigurationTemplate,
        value: String,
    ): Boolean {
        var isTypeValid = true
        if (field.type == "AlphaNumeric" && value.isNotEmpty() && !isAlphaNumeric(value)) {
            isTypeValid = false
        } else if (field.type == "Alphabets" && value.isNotEmpty() && !isAlphabetic(value)) {
            isTypeValid = false
        } else if (field.type == "Number" && value.isNotEmpty() && !isNumeric(value)) {
            isTypeValid = false
        } else if (field.type == "Date Time" && value.isNotEmpty() && !checkDateOrTimeFormat(field.datetime, value)) {
            isTypeValid = false
        } else if (field.type == "Date" && value.isNotEmpty() && !checkDateOrTimeFormat(field.date, value)) {
            isTypeValid = false
        }
        else if (field.type == "Email" && value.isNotEmpty() && !isEmail(value)) {
            isTypeValid = false
        } else if (field.type == "Floating Number" && value.isNotEmpty() && !isFloatingNumber(value)) {
            isTypeValid = false
        }
        else if (field.type == "Special Characters" && value.isNotEmpty() && !hasSpecialCharacters(value)) {
        isTypeValid = false
        }


        return isTypeValid
    }

    private fun getFieldValues(
        fieldArray: Array<ConfigurationTemplate>,
        key: String,
        ele: JSONObject,
    ): Pair<ConfigurationTemplate, String> {
        val field = fieldArray.first { it.fieldName == key }
        val value = ele.get(key) as String
        return Pair(field, value)
    }

    private fun getElementKeys(element: Any?): Pair<JSONObject, MutableSet<String>> {
        val ele = (element as JSONObject)
        val keys = ele.keySet()
        return Pair(ele, keys)
    }

    private fun getErrorMessages(
        isLengthValid: Boolean,
        index: Int,
        field: ConfigurationTemplate,
        typeErrors: JSONArray,
    ) {
        if (!isLengthValid) {
            var errorMsg = "Incorrect Type of ${field.fieldName}. Please change to ${field.type}"
            if(field.type == "Date"){
                errorMsg = "Incorrect Type of ${field.fieldName}. Please change  ${field.type} format to ${field.date}"
            }

            val jsonObject = JSONObject().put(
                (index + 1).toString(), errorMsg
            )
            typeErrors.put(jsonObject)
        }
    }

    fun isFloatingNumber(value: String): Boolean {
        return value.matches( "^[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)\$".toRegex())
    }

    fun isNumeric(value: String): Boolean {
        return value.matches("-?\\d+(\\.\\d+)?".toRegex())
    }

    fun isAlphabetic(value: String): Boolean {
        return value.all { it.isLetter() }
    }

    fun isAlphaNumeric(value: String): Boolean {
        return value.all { it.isLetterOrDigit() }
    }

    fun hasSpecialCharacters(value:String):Boolean{
        val emailPattern = Regex("^[a-zA-Z0-9_,@\\s]+\$")
        return emailPattern.matches(value)
    }

    fun isEmail(value: String): Boolean {
        val emailPattern = Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")
        return emailPattern.matches(value)
    }

    fun checkDateOrTimeFormat(dateTimeFormat:String?, value: String): Boolean {
        val sdf: DateFormat = SimpleDateFormat(dateTimeFormat as String)
        sdf.isLenient = false
        try {
            sdf.parse(value.trim())
        } catch (e: ParseException) {
            return false
        }
        return true
    }


}