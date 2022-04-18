fun main() {
//    Server(3002).startServer()
//    var s = "01/12/1998 13:20"
//    var t = "^([0]d|[1][0-2])/([0-2]d|[3][0-1])/([2][01]|[1][6-9])d{2}(s([0-1]d|[2][0-3])(:[0-5]d){1,2})?$".toRegex()
    var dateTimeFormat = "01:00"
    var pattern = "^(3[0-1]|2[0-9]|1[0-9]|0[1-9])(0[0-9]|1[0-9]|2[0-3])([0-5][0-9])\\sUTC\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s[0-9]{2}\$".toRegex()
    println(dateTimeFormat.matches(pattern))
    var pattern2 = "((\\(\\d{2}\\) ?)|(\\d{2}/))?\\d{2}/\\d{4} ([0-2][0-9]\\:[0-6][0-9])"
}
