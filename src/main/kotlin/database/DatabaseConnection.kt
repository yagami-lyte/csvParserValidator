package database


import java.sql.Connection
import java.sql.DriverManager

object DatabaseConnection  {
    fun makeConnection() : Connection {
        val user = "root"
        val password = "root"
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/DEMO",user, password)
    }
}