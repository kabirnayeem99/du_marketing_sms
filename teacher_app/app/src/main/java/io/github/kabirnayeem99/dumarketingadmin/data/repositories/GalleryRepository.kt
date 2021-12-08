package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.BaasService
import io.github.kabirnayeem99.dumarketingadmin.data.vo.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.data.vo.GalleryData.Companion.toGalleryDataList
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.GALLERY_DB_REF
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.GALLERY_IMAGE_PATH_STRING_FOLDER_NAME
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GalleryRepository @Inject constructor(var db: FirebaseFirestore, var store: FirebaseStorage) {

    private val galleryStorageRef = store.reference.child(GALLERY_IMAGE_PATH_STRING_FOLDER_NAME)


    fun saveGalleryData(galleryData: GalleryData): Task<Void> {

        lateinit var key: String

        if (galleryData.key == null) {
            key = System.currentTimeMillis().toString()
            galleryData.key = key
        } else {
            galleryData.key?.let {
                key = it
            }
        }

        return db.collection(GALLERY_DB_REF).document(key).set(galleryData)
    }

    fun uploadGalleryImage(category: String, imageFile: ByteArray): UploadTask {

        val galleryCatStorageRef = galleryStorageRef.child(category)

        val filePath = galleryCatStorageRef.child("$category-$imageFile.jpg")

        return filePath.putBytes(imageFile)

    }


    suspend fun deleteGalleryData(galleryData: GalleryData): Boolean {

        try {

            val key = galleryData.key!!

            // deletes from firebase storage
            BaasService.storage.getReferenceFromUrl(galleryData.imageUrl).delete().await()

            // deletes from firebase db
            db.collection(GALLERY_DB_REF).document(key).delete().await()

            return true

        } catch (e: Exception) {
            Log.e(TAG, "deleteGalleryImage: $e")

            return false
        }

    }

    private val galleryImageLiveData = MutableLiveData<Resource<List<GalleryData>>>()

    fun getGalleryDataListObservable(): LiveData<Resource<List<GalleryData>>> {

        galleryImageLiveData.value = Resource.Loading()

        db.collection(GALLERY_DB_REF).addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    galleryImageLiveData.value =
                        Resource.Error(error.message ?: "Unknown error")
                    return@EventListener
                }

                if (value != null) {
                    val galleryDataList = value.toGalleryDataList()
                    galleryImageLiveData.value = Resource.Success(galleryDataList)
                }
            }
        )

        return galleryImageLiveData
    }

    companion object {
        private const val TAG = "GalleryRepository"
    }
}