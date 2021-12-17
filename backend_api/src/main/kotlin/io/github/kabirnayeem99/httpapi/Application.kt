package io.github.kabirnayeem99.httpapi

import io.github.kabirnayeem99.httpapi.routes.registerInformationRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    registerInformationRoutes()
}
