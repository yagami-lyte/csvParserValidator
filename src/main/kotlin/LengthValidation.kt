class LengthValidation {

    fun maxLength(data: String, length: Int):Boolean{
        return length > data.length
    }

    fun minLength(data: String,length: Int):Boolean{
        return length < data.length
    }

    fun fixedLenght(data: String,length: Int):Boolean{
        return length == data.length
    }
}