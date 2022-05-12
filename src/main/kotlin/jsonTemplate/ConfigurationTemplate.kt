package jsonTemplate

class ConfigurationTemplate(
    val csvName: String,
    val fieldName: String,
    val type: String?,
    val length: String?,
    val datetime: String?,
    val date: String?,
    val time: String?,
    val values: List<String>?,
    val dependentOn: String,
    val dependentValue: String,
    val nullValue: String
)