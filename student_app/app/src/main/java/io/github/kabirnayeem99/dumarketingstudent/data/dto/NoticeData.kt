package io.github.kabirnayeem99.dumarketingstudent.data.dto

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


data class NoticeData(
    var title: String,
    var imageUrl: String,
    var date: String,
    var time: String,
    var key: String? = null,
) {
    companion object {
        /**
         * Kotlin Extension function to transform
         * a cloud firestore [QueryDocumentSnapshot]
         * into a [NoticeData] object
         *
         * @return a [NoticeData] data object
         */
        private fun QueryDocumentSnapshot.toNoticeData(): NoticeData {
            val title: String = this["title"].toString()
            val imageUrl: String = this["imageUrl"].toString()
            val date: String = this["date"].toString()
            val time: String = this["time"].toString()
            val key: String = this["key"].toString()

            return NoticeData(title, imageUrl, date, time, key)
        }

        /**
         * Kotlin Extension function to transform
         * a cloud firestore [QuerySnapshot]
         * into a list of [NoticeData] objects
         *
         * @return a list of [NoticeData] data objects
         */
        fun QuerySnapshot.toNoticeDataList(): List<NoticeData> {
            val noticeDataList = mutableListOf<NoticeData>()

            for (data in this) {
                noticeDataList.add(data.toNoticeData())
            }

            return noticeDataList
        }
    }
}