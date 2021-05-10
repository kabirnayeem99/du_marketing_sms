package io.github.kabirnayeem99.dumarketingstudent.data.vo

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class RoutineData(
    val time: String,
    val className: String,
    val classLocation: String,
    val facultyName: String
) {
    companion object {
        private fun DocumentSnapshot.toRoutineData(): RoutineData {
            val time = this["time"].toString()
            val className = this["className"].toString()
            val classLocation = this["classLocation"].toString()
            val facultyName = this["facultyName"].toString()
            return RoutineData(time, className, classLocation, facultyName)
        }

        fun QuerySnapshot.toRoutineDataList(): List<RoutineData> {
            val routineDataList = mutableListOf<RoutineData>()

            for (docSnapshot in this) {
                routineDataList.add(docSnapshot.toRoutineData())
            }

            return routineDataList
        }
    }
}
