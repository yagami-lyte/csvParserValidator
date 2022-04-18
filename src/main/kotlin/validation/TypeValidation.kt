package validation

import jsonTemplate.ConfigurationTemplate
import org.json.JSONArray
import org.json.JSONObject

class TypeValidation : Validation {

    override fun validate(jsonArrayData: JSONArray, fieldArray: Array<ConfigurationTemplate>): JSONArray {
        val typeErrors = JSONArray()
        val typeValidation = TypeValidation()
        jsonArrayData.forEachIndexed { index, element ->
            val (ele, keys) = getElementKeys(element)
            for (key in keys) {
                val (field, value) = getFieldValues(fieldArray, key, ele)
                var isLengthValid = validateTypeInEachRow(field, value, typeValidation)
                getErrorMessages(isLengthValid, index, field, typeErrors)
            }
        }
        return typeErrors
    }

    private fun validateTypeInEachRow(
        field: ConfigurationTemplate,
        value: String,
        typeValidation: TypeValidation
    ): Boolean {
        var isLengthValid = true
        if (field.type == "AlphaNumeric" && value.isNotEmpty() && !typeValidation.isAlphaNumeric(value)) {
            isLengthValid = false
        } else if (field.type == "Alphabet" && value.isNotEmpty() && !typeValidation.isAlphabetic(value)) {
            isLengthValid = false
        } else if (field.type == "Number" && value.isNotEmpty() && !typeValidation.isNumeric(value)) {
            isLengthValid = false
        } else if (field.type == "Date Time" && value.isNotEmpty() && !typeValidation.isProperDateTimeFormat(
                field.datetime,
                value
            )
        ) {
            isLengthValid = false
        }
        return isLengthValid
    }

    private fun getFieldValues(
        fieldArray: Array<ConfigurationTemplate>,
        key: String,
        ele: JSONObject
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
        typeErrors: JSONArray
    ) {
        if (!isLengthValid) {
            var errorMsg = "Incorrect Type of ${field.fieldName}. Please change to ${field.type}";
            if(field.type == "Date Time"){
                errorMsg = "Incorrect Type of ${field.fieldName}. Please change  ${field.type} format to ${field.datetime}";
            }

            val jsonObject = JSONObject().put(
                (index + 1).toString(), errorMsg
            )
            typeErrors.put(jsonObject)
        }
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

    fun isProperDateTimeFormat(dateTimeFormat: String?, value: String): Boolean {
        val dateFormat = giveDateFormat(dateTimeFormat);
        return value.matches(dateFormat!!);
    }

    fun giveDateFormat(format: String?): Regex? {
        val dateFormats = mapOf(
            "MM/DD/YYYY" to "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$".toRegex(),
            "DD/MM/YYYY" to "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/([0-9][0-9])?[0-9][0-9]$".toRegex(),
            "YYYY/MM/DD" to "^(d{4})-((0[1-9])|(1[0-2]))-(0[1-9]|[12][0-9]|3[01])$".toRegex(),
            "DD/MM/YYYY HH:MM:SS AM" to "^(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/][0-9]{4}(\\s((0[1-9]|1[012])\\:([0-5][0-9])((\\s)|(\\:([0-5][0-9])\\s))([AM|PM|]{2,2})))?\$".toRegex(),
            "HH:MM" to  "^(([01][0-9]|[012][0-3]):([0-5][0-9]))*\$".toRegex(),
            "DDHHMM UTC Jun YY" to "^(3[0-1]|2[0-9]|1[0-9]|0[1-9])(0[0-9]|1[0-9]|2[0-3])([0-5][0-9])\\sUTC\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s[0-9]{2}\$".toRegex(),
            "DD/MM/YYYY HH:MM" to "((\\(\\d{2}\\) ?)|(\\d{2}/))?\\d{2}/\\d{4} ([0-2][0-9]\\:[0-6][0-9])".toRegex(),
            "Jul 30,2015 10:40:43 AM" to "^(?:(((Jan(uary)?|Ma(r(ch)?|y)|Jul(y)?|Aug(ust)?|Oct(ober)?|Dec(ember)?)\\ 31)|((Jan(uary)?|Ma(r(ch)?|y)|Apr(il)?|Ju((ly?)|(ne?))|Aug(ust)?|Oct(ober)?|(Sept|Nov|Dec)(ember)?)\\ (0?[1-9]|([12]\\d)|30))|(Feb(ruary)?\\ (0?[1-9]|1\\d|2[0-8]|(29(?=,\\ ((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)))))))\\,((1[6-9]|[2-9]\\d)\\d{2})) (?:[0-1]?[0-9]|[2][1-4]):[0-5]?[0-9]:[0-5]?[0-9]\\s?([apAP][Mm])?\$".toRegex()
        )
        return dateFormats[format];
    }
}