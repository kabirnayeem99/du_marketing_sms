package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denzcoskun.imageslider.models.SlideModel
import io.github.kabirnayeem99.dumarketingstudent.data.vo.GalleryData

class GalleryRepository {

    private val recentGallerySlideModelLiveData = MutableLiveData<List<SlideModel>>()
    private val recentGalleryDataLiveData = MutableLiveData<List<GalleryData>>()

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

        recentGallerySlideModelLiveData.value = imageList

        return recentGallerySlideModelLiveData
    }

    fun getGalleryImages(): LiveData<List<GalleryData>> {
        val imageList = ArrayList<GalleryData>()

        imageList.add(
            GalleryData(
                "https://www.campusvarta.com/wp-content/uploads/2020/01/dhaka_university.jpg",
            )
        )

        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/9/94/16122009_Dhaka_University_Mosque_photo1_Ranadipam_Basu.jpg",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://www.campusvarta.com/wp-content/uploads/2020/01/dhaka_university.jpg",
            )
        )

        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )

        imageList.add(
            GalleryData(
                "https://www.campusvarta.com/wp-content/uploads/2020/01/dhaka_university.jpg",
            )
        )

        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )

        imageList.add(
            GalleryData(
                "https://www.campusvarta.com/wp-content/uploads/2020/01/dhaka_university.jpg",
            )
        )

        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )
        imageList.add(
            GalleryData(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )

        recentGalleryDataLiveData.value = imageList

        return recentGalleryDataLiveData
    }
}