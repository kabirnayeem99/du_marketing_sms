package io.github.kabirnayeem99.dumarketingstudent.data.vo

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class FacultyData(
    var name: String,
    var phone: String,
    var email: String,
    var post: String,
    var profileImageUrl: String? = null,
    var key: String? = null,
) {
    companion object {
        private fun QueryDocumentSnapshot.toFacultyData(): FacultyData {
            val post: String = this["post"].toString()
            val name: String = this["name"].toString()
            val email: String = this["email"].toString()
            val phone: String = this["phone"].toString()
            val profileImageUrl: String = this["profileImageUrl"].toString()
            val key: String = this["key"].toString()

            return FacultyData(name, phone, email, post, profileImageUrl, key)
        }

        fun QuerySnapshot.toFacultyDataList(): List<FacultyData> {
            val facultyDataList = mutableListOf<FacultyData>()

            for (data in this) {
                facultyDataList.add(data.toFacultyData())
            }

            return facultyDataList
        }
    }
}
