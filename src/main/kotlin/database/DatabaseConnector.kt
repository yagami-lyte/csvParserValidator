package database

import java.sql.Connection

interface DatabaseConnector {
    fun makeConnection() : Connection
}