package com.wolt.httpapi

import com.wolt.httpapi.routes.registerRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*

/**
 * Entry Point of the application. This function is referenced in the
 * resources/application.conf file inside the ktor.application.modules.
 */
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    registerRoutes()
}

