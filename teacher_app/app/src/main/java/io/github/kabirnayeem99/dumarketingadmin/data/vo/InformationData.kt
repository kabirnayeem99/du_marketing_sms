package io.github.kabirnayeem99.dumarketingadmin.data.vo

import android.util.Log
import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentSnapshot

@Keep
data class InformationData(
    var intro: String,
    var email: String,
    var telephone: String,
    var lat: Double,
    var long: Double,
) {
    companion object {
        fun DocumentSnapshot.toInformationData(): InformationData {

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


            return InformationData(intro, email, telephone, lat, long)

        }

        private const val TAG = "InformationData"
    }

}
