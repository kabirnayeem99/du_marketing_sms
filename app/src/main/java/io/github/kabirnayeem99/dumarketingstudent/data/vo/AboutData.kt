package io.github.kabirnayeem99.dumarketingstudent.data.vo

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

data class AboutData(
    var intro: String,
    var email: String,
    var telephone: String,
    var lat: Double,
    var long: Double,
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
                Log.e(TAG, "toInformationData: ${e.message}")
                e.printStackTrace()
            }

            val intro = this["intro"].toString()


            return AboutData(intro, email, telephone, lat, long)

        }

        private const val TAG = "AboutData"
    }

}