package io.github.kabirnayeem99.dumarketingstudent.data.vo

import com.google.firebase.firestore.DocumentSnapshot
import timber.log.Timber

data class AboutData(
    var intro: String,
    var email: String,
    var telephone: String,
    var lat: Double,
    var long: Double
) {
    companion object {
        fun DocumentSnapshot.toAboutData(): AboutData {

            var lat = 0.0
            var long = 0.0

            val email = this["email"].toString()


            val telephone = this["telephone"].toString()

            try {
                lat = this["lat"].toString().toDouble()
                long = this["long"].toString().toDouble()
            } catch (e: Exception) {
                Timber.e(e)
            }

            val intro = this["intro"].toString()


            return AboutData(intro, email, telephone, lat, long)

        }

    }

}