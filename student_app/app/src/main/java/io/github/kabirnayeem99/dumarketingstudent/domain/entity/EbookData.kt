package io.github.kabirnayeem99.dumarketingstudent.domain.entity

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class EbookData(
    var title: String,
    var pdfUrl: String,
    var key: String? = null
) {
    companion object {
        private fun DocumentSnapshot.toEbookData(): EbookData {
            val title = this["title"].toString()
            val pdfUrl = this["pdfUrl"].toString()
            val key = this["key"].toString()
            return EbookData(title, pdfUrl, key)
        }

        fun QuerySnapshot.toEbookDataList(): List<EbookData> {
            val ebookDataList = mutableListOf<EbookData>()

            for (docSnapshot in this) {
                ebookDataList.add(docSnapshot.toEbookData())
            }

            return ebookDataList
        }
    }
}