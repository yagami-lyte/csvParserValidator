package database


import java.sql.Connection
import java.sql.DriverManager

class Connector : DatabaseConnector {
    override fun makeConnection(): Connection {
        val user = "root"
        val password = "root"
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/DEMO", user, password)
    }
}