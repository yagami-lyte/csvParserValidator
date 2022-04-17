package jsonTemplate

class ConfigurationTemplate(
    val fieldName: String?,
    val type: String?,
    val length: String?,
    val values: List<String>?,
    val dependentOn: String,
    val dependentValue: String
)

