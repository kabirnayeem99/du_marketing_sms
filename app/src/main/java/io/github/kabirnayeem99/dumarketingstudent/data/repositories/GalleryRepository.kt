package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denzcoskun.imageslider.models.SlideModel

class GalleryRepository {

    private val recentGalleryLiveData = MutableLiveData<List<SlideModel>>()

    fun getRecentGallerySlideModel(): LiveData<List<SlideModel>> {
        val imageList = ArrayList<SlideModel>()

        imageList.add(
            SlideModel(
                "https://www.campusvarta.com/wp-content/uploads/2020/01/dhaka_university.jpg",
            )
        )

        imageList.add(
            SlideModel(
                "https://upload.wikimedia.org/wikipedia/commons/9/94/16122009_Dhaka_University_Mosque_photo1_Ranadipam_Basu.jpg",
            )
        )
        imageList.add(
            SlideModel(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )

        recentGalleryLiveData.value = imageList

        return recentGalleryLiveData
    }
}