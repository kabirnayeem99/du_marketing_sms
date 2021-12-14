package io.github.kabirnayeem99.dumarketingadmin.data.model

import android.os.Parcelable
import android.util.Log
import androidx.annotation.Keep
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@Parcelize
@Keep
data class FacultyData(
    var name: String,
    var phone: String,
    var email: String,
    var post: String,
    var profileImageUrl: String? = null,
    var key: String? = null,
) : Parcelable {
    companion object {

        /**
         * This  extension function transforms the [DataSnapshot] of
         * list of [FacultyData] into the
         * list of [FacultyData]
         *
         * @return list of [FacultyData]
         */
        fun DataSnapshot.toFacultyDataList(): MutableList<FacultyData> {
            val facultyDataList = mutableListOf<FacultyData>()

            for (postSnapshot: DataSnapshot in children) {

                val facultyDataHashMap = postSnapshot.value as HashMap<String, String>
                val facultyData = facultyDataHashMap.toFacultyData()
                facultyDataList.add(facultyData)
            }

            return facultyDataList
        }

        /**
         * This  extension function transforms the [QuerySnapshot]
         * of list of [FacultyData] into the
         * list of [FacultyData]
         *
         * @return list of [FacultyData]
         */
        fun QuerySnapshot.toFacultyDataList(): List<FacultyData> {
            val facultyDataList = mutableListOf<FacultyData>()

            for (data in this) {
                val facultyData = data.toFacultyData()
                facultyDataList.add(data.toFacultyData())
                Timber.d("toFacultyDataList: $facultyData")
            }

            return facultyDataList
        }

        private fun QueryDocumentSnapshot.toFacultyData(): FacultyData {
            val post: String = this["post"].toString()
            val name: String = this["name"].toString()
            val email: String = this["email"].toString()
            val phone: String = this["phone"].toString()
            val profileImageUrl: String = this["profileImageUrl"].toString()
            val key: String = this["key"].toString()

            return FacultyData(name, phone, email, post, profileImageUrl, key)
        }


        private fun HashMap<String, String>.toFacultyData(): FacultyData {
            val post: String = this["post"] ?: ""
            val name = this["name"] ?: ""
            val email = this["email"] ?: ""
            val phone = this["phone"] ?: ""
            val profileImageUrl = this["profileImageUrl"] ?: ""
            val key = this["key"] ?: ""

            return FacultyData(name, phone, email, post, profileImageUrl, key)
        }

        private const val TAG = "FacultyData"

    }

}
