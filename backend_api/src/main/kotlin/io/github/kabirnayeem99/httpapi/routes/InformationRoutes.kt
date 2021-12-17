package io.github.kabirnayeem99.httpapi.routes

import io.github.kabirnayeem99.httpapi.models.Information
import io.github.kabirnayeem99.httpapi.models.information
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.informationRouteing() {
    route("/information") {
        get {
            if (information.lat != 0.0 && information.long != 0.0) {
                call.respond(information)
            } else {
                call.respondText("No information is saved yet.\n", status = HttpStatusCode.NotFound)
            }
        }

        post {
            val receivedInformation = call.receive<Information>()
            information = receivedInformation
            call.respondText("Information is saved successfully.", status = HttpStatusCode.Created)
        }
    }
}

fun Application.registerInformationRoutes() {
    routing {
        informationRouteing()
    }
}