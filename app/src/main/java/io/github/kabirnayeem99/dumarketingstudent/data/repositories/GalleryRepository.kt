package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.data.vo.GalleryData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.GalleryData.Companion.toGalleryDataList
import io.github.kabirnayeem99.dumarketingstudent.data.vo.GalleryData.Companion.toSlideModelList
import io.github.kabirnayeem99.dumarketingstudent.util.Constants
import io.github.kabirnayeem99.dumarketingstudent.util.enums.GalleryImageCategory

class GalleryRepository {

    private val db = FirebaseFirestore.getInstance()

    private val recentGallerySlideModelLiveData = MutableLiveData<List<SlideModel>>()
    private val allGalleryImagesLiveData = MutableLiveData<List<GalleryData>>()

    fun getRecentGallerySlideModel(): LiveData<List<SlideModel>> {
        val imageList = ArrayList<SlideModel>()

        val refConvocation =
            db.collection(Constants.GALLERY_DB_REF)
                .document(GalleryImageCategory.CONVOCATION.values)
                .collection("images")

        refConvocation.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getGalleryImages: ${error.message}")
                    return@EventListener
                }

                if (value != null) {
                    imageList.addAll(value.toSlideModelList())
                    recentGallerySlideModelLiveData.value = imageList
                }
            }
        )


        recentGallerySlideModelLiveData.value = imageList

        return recentGallerySlideModelLiveData
    }

    // a mess i should take care of
    fun getGalleryImages(): LiveData<List<GalleryData>> {
        val imageList = mutableListOf<GalleryData>()

        val refClub =
            db.collection(Constants.GALLERY_DB_REF).document(GalleryImageCategory.CLUB.values)
                .collection("images")

        refClub.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getGalleryImages: ${error.message}")
                    return@EventListener
                }

                if (value != null) {
                    imageList.addAll(value.toGalleryDataList())
                    allGalleryImagesLiveData.value = imageList
                }
            }
        )
        val refOther =
            db.collection(Constants.GALLERY_DB_REF).document(GalleryImageCategory.OTHER.values)
                .collection("images")

        refOther.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getGalleryImages: ${error.message}")
                    return@EventListener
                }

                if (value != null) {
                    imageList.addAll(value.toGalleryDataList())
                    allGalleryImagesLiveData.value = imageList
                }
            }
        )
        val refClass =
            db.collection(Constants.GALLERY_DB_REF).document(GalleryImageCategory.CLASS.values)
                .collection("images")

        refClass.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getGalleryImages: ${error.message}")
                    return@EventListener
                }

                if (value != null) {
                    imageList.addAll(value.toGalleryDataList())
                    allGalleryImagesLiveData.value = imageList
                }
            }
        )
        val refConvocation =
            db.collection(Constants.GALLERY_DB_REF)
                .document(GalleryImageCategory.CONVOCATION.values)
                .collection("images")

        refConvocation.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getGalleryImages: ${error.message}")
                    return@EventListener
                }

                if (value != null) {
                    imageList.addAll(value.toGalleryDataList())
                    allGalleryImagesLiveData.value = imageList
                }
            }
        )

        return allGalleryImagesLiveData
    }

    companion object {
        private const val TAG = "GalleryRepository"
    }
}