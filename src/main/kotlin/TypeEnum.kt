import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

enum class TypeEnum {
    AlphaNumeric {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            return value.all { it.isLetterOrDigit() }
        }
    },
    Alphabets {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            return value.all { it.isLetter() }
        }
    },
    Number {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            return value.matches("-?\\d+(\\.\\d+)?".toRegex())
        }
    },
    DateTime {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            val sdf: DateFormat = SimpleDateFormat(pattern as String)
            sdf.isLenient = false
            try {
                sdf.parse(value.trim())
            } catch (e: ParseException) {
                return false
            }
            return true
        }
    },
    Date {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            val sdf: DateFormat = SimpleDateFormat(pattern as String)
            sdf.isLenient = false
            try {
                sdf.parse(value.trim())
            } catch (e: ParseException) {
                return false
            }
            return true
        }
    },
    Time {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            val sdf: DateFormat = SimpleDateFormat(pattern as String)
            sdf.isLenient = false
            try {
                sdf.parse(value.trim())
            } catch (e: ParseException) {
                return false
            }
            return true
        }
    },
    Email {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            val emailPattern = Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")
            return emailPattern.matches(value)
        }
    },
    FloatingNumber {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            return value.matches("[+-]?([0-9]*[.])?[0-9]+".toRegex())
        }
    },
    Text {
        override fun typeCheck(value: String, pattern: String?): Boolean {
            return true
//        val allKeyboardKeysRegex = ("""[A-Za-z0-9-]+[ 0-9A-Za-z#$%=@!{},`~&*()'<>?.:;_|^/+\t\r\n\[\]"-]*""").toRegex();
//        return allKeyboardKeysRegex.matches(value)
        }
    };

    abstract fun typeCheck(value: String, pattern: String? = null): Boolean
}

