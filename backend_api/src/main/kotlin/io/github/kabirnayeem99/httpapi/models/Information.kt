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

var information: Information = Information(
    intro = "The Department of Marketing, one of the members of the Faculty " +
            "of Business Studies, was created on 1st July, 1974. " +
            "This department was established with a view to fulfilling the " +
            "need for specialized education, research and training in the " +
            "field of marketing. Presently, there are about 1200 students in " +
            "its Bachelor, Master, M.Phil and Ph.D. programmes in the department.",
    lat = 5.63424,
    long = 66.921,
    telephone = "880-2-8611996- (7954)",
    email = "marketing@du.ac.bd"
)