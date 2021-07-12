package io.github.kabirnayeem99.dumarketingadmin.data.vo

import androidx.annotation.Keep
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

@Keep
data class NoticeData(
    var title: String,
    var imageUrl: String,
    var date: String,
    var time: String,
    var key: String? = null,
) {
    companion object {

        /**
         * This function turns the [QuerySnapshot] of list of notice data
         * into the list of notice data
         *
         * @return list of [NoticeData]
         */
        fun QuerySnapshot.toNoticeDataList(): List<NoticeData> {
            val noticeDataList = mutableListOf<NoticeData>()

            for (data in this) {
                noticeDataList.add(data.toNoticeData())
            }

            return noticeDataList
        }

        private fun QueryDocumentSnapshot.toNoticeData(): NoticeData {
            val title: String = this["title"].toString()
            val imageUrl: String = this["imageUrl"].toString()
            val date: String = this["date"].toString()
            val time: String = this["time"].toString()
            val key: String = this["key"].toString()

            return NoticeData(title, imageUrl, date, time, key)
        }


    }
}
