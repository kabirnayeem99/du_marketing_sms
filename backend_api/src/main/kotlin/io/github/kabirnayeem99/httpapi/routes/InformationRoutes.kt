package io.github.kabirnayeem99.httpapi.routes

import io.github.kabirnayeem99.httpapi.models.informationStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.informationRouteing() {
    route("/information") {
        get {
            if (informationStorage.isNotEmpty()) {
                call.respond(informationStorage)
            } else {
                call.respondText("No information found\n", status = HttpStatusCode.NotFound)
            }
        }
    }
}

fun Application.registerInformationRoutes() {
    routing {
        informationRouteing()
    }
}