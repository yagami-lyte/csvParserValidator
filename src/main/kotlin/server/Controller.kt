package server

import database.Connector
import database.DatabaseOperations
import org.springframework.web.bind.annotation.*
import routeHandler.RouteHandler
import routeHandler.getRouteHandler.getResponse.ConfigNames
import routeHandler.getRouteHandler.getResponse.ErrorPage
import routeHandler.getRouteHandler.getResponse.HomePage
import routeHandler.postRouteHandler.postResponse.HandleCSVMetaData
import routeHandler.postRouteHandler.postResponse.HandleCsv
import routeHandler.postRouteHandler.postResponse.SendConfigurations


@RestController
class GreetingController {

    private val configNames = ConfigNames(DatabaseOperations(Connector()))
    private val errorPage = ErrorPage()
    private val homePage = HomePage()
    //    private val server = Server()
//    private val routeHandler = RouteHandler()
////    private val extractor = Extractor()
//
    private val handleCsv = HandleCsv()
    private val handleAddingCsvMetaData = HandleCSVMetaData()
    private val sendConfigurations = SendConfigurations(DatabaseOperations(Connector()))


    @GetMapping("/")
    fun getHTML() :String {
        return homePage.getResponse("/index.html")
    }

    @GetMapping("/main.js")
    fun getJs() :String {
        return homePage.getResponse("/main.js")
    }

    @GetMapping("/main.css")
    fun getCSS() :String {
        return homePage.getResponse("/main.css")
    }

    @PostMapping("/add-meta-data")
    fun postBody(@RequestBody configData: String): String {
        return handleAddingCsvMetaData.postResponse(configData)
    }

    @PostMapping("/csv")
    fun postCSV(@RequestBody csvData: String): String {
        return handleCsv.postResponse(csvData)
    }

    @PostMapping("/get-config-response")
    fun postGetConfigResponse(@RequestBody csvData: String): String {
        return sendConfigurations.postResponse(csvData)
    }

}