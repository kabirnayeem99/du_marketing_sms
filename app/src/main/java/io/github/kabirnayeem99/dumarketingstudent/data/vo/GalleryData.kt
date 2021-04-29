package io.github.kabirnayeem99.dumarketingstudent.data.vo

import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


data class GalleryData(
    val imageUrl: String,
    var category: String,
    var key: String? = null,
) {
    companion object {
        private fun QueryDocumentSnapshot.toGalleryData(): GalleryData {
            val imageUrl = this["imageUrl"].toString()
            val category = this["category"].toString()

            return GalleryData(imageUrl = imageUrl, category = category)
        }

        fun QuerySnapshot.toGalleryDataList(): List<GalleryData> {
            val galleryDataList = mutableListOf<GalleryData>()

            for (data in this) {
                galleryDataList.add(data.toGalleryData())
            }
            return galleryDataList
        }

        fun QueryDocumentSnapshot.toSlideModel(): SlideModel {
            val imageUrl = this["imageUrl"].toString()
            return SlideModel(imageUrl, "")
        }

        fun QuerySnapshot.toSlideModelList(): List<SlideModel> {
            val slideModelList = mutableListOf<SlideModel>()

            for (data in this) {
                slideModelList.add(data.toSlideModel())
            }

            return slideModelList
        }
    }
}