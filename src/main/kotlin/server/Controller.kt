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

}