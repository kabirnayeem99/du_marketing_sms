package io.github.kabirnayeem99.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class Information(
    var intro: String = "",
    var email: String = "",
    var telephone: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0,
)

val informationStorage = mutableListOf<Information>(Information("Intro", "email", "telephone", 1829.00, 2920.00))