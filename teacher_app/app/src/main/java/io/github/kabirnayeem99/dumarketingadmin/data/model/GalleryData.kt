package io.github.kabirnayeem99.dumarketingadmin.data.model

import androidx.annotation.Keep
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

@Keep
data class GalleryData(
    var category: String,
    var imageUrl: String,
    var key: String? = null,
) {
    companion object {
        private fun QueryDocumentSnapshot.toGalleryData(): GalleryData {
            val imageUrl = this["imageUrl"].toString()
            val category = this["category"].toString()
            val key = this["key"].toString()

            return GalleryData(imageUrl = imageUrl, category = category, key = key)
        }

        fun QuerySnapshot.toGalleryDataList(): List<GalleryData> {
            val galleryDataList = mutableListOf<GalleryData>()

            for (data in this) {
                galleryDataList.add(data.toGalleryData())
            }
            return galleryDataList
        }
    }
}
