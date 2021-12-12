package io.github.kabirnayeem99.dumarketingadmin.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RoutineData(
    var time: String,
    var className: String,
    var classLocation: String,
    var facultyName: String
) : Parcelable {

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

