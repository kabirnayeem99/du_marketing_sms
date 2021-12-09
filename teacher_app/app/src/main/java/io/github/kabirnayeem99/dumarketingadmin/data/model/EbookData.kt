package io.github.kabirnayeem99.dumarketingadmin.data.model

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

@Keep
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

        fun QuerySnapshot.toEbookDataList(): MutableList<EbookData> {
            val ebookDataList = mutableListOf<EbookData>()

            for (docSnapshot in this) {
                ebookDataList.add(docSnapshot.toEbookData())
            }

            return ebookDataList
        }
    }
}
