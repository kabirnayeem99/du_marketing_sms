package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.MutableLiveData
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.data.vo.GalleryData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.GalleryData.Companion.toGalleryDataList
import io.github.kabirnayeem99.dumarketingstudent.data.vo.GalleryData.Companion.toSlideModelList
import io.github.kabirnayeem99.dumarketingstudent.util.Constants
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import javax.inject.Inject

class GalleryRepository @Inject constructor(var db: FirebaseFirestore) {


    fun getRecentGallerySlideModel(): MutableLiveData<Resource<List<SlideModel>>> {

        val recentGallerySlideModelLiveData: MutableLiveData<Resource<List<SlideModel>>> by lazy {
            MutableLiveData<Resource<List<SlideModel>>>()
        }

        recentGallerySlideModelLiveData.value = Resource.Loading()

        val ref = db.collection(Constants.GALLERY_DB_REF)
            .orderBy("category")
            .orderBy("key")
            .limit(4)

        ref.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    recentGallerySlideModelLiveData.value = Resource.Error(
                        error.message ?: "Unknown error"
                    )

                    return@EventListener
                }

                if (value != null) {
                    val imageList = value.toSlideModelList()

                    recentGallerySlideModelLiveData.value = Resource.Success(imageList)
                }
            }
        )

        return recentGallerySlideModelLiveData
    }

    fun getGalleryImages(): MutableLiveData<Resource<List<GalleryData>>> {

        val allGalleryImagesLiveData: MutableLiveData<Resource<List<GalleryData>>> by lazy {
            MutableLiveData<Resource<List<GalleryData>>>()
        }
        val ref =
            db.collection(Constants.GALLERY_DB_REF)
                .orderBy("category")
                .orderBy("key")

        ref.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    allGalleryImagesLiveData.value = Resource.Error(error.message ?: "Unknown")
                    return@EventListener
                }

                if (value != null) {
                    val imageList = value.toGalleryDataList()
                    allGalleryImagesLiveData.value = Resource.Success(imageList)
                }
            }
        )

        return allGalleryImagesLiveData
    }

}