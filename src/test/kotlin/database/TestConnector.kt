package database

import java.sql.Connection
import java.sql.DriverManager

class TestConnector: DatabaseConnector {
    override fun makeConnection(): Connection {
        return DriverManager.getConnection("jdbc:h2:~/db;MODE=MYSQL;INIT=RUNSCRIPT FROM 'src/test/kotlin/database/TestDatabaseTables.sql'")
    }
}