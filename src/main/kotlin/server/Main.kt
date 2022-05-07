package server

import database.DatabaseConnection


fun main() {
    DatabaseConnection.makeConnection();
    //Server(3002).startServer()
}
