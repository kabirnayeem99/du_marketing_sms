package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData.Companion.toGalleryDataList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class GalleryDataSource
@Inject constructor(
    private var db: FirebaseFirestore,
    store: FirebaseStorage,
) {

    private val galleryStorageRef =
        store.reference.child(Constants.GALLERY_IMAGE_PATH_STRING_FOLDER_NAME)


    suspend fun saveGalleryData(galleryData: GalleryData): Resource<String> {
        try {
            lateinit var key: String

            if (galleryData.key == null) {
                key = System.currentTimeMillis().toString()
                galleryData.key = key
            } else {
                galleryData.key?.let {
                    key = it
                }
            }
            db.collection(Constants.GALLERY_DB_REF).document(key).set(galleryData).await()
            return Resource.Success(key)
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: "Could not save gallery.")
        }
    }

    suspend fun uploadGalleryImage(category: String, imageFile: ByteArray): Resource<String> {
        return try {
            val galleryCatStorageRef = galleryStorageRef.child(category)
            val filePath = galleryCatStorageRef.child("$category-$imageFile.jpg")
            filePath.putBytes(imageFile).await()
            Resource.Success(category)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not upload the image in $category.")
        }
    }


    suspend fun deleteGalleryData(galleryData: GalleryData): Resource<String> {
        return try {
            val key = galleryData.key!!
            // deletes from firebase storage
            BaasService.storage.getReferenceFromUrl(galleryData.imageUrl).delete().await()
            // deletes from firebase db
            db.collection(Constants.GALLERY_DB_REF).document(key).delete().await()
            Resource.Success(key)
        } catch (e: Exception) {
            Timber.e("deleteGalleryImage: $e")
            Resource.Error(e.localizedMessage ?: "Could not delete the image.")
        }
    }

    @ExperimentalCoroutinesApi
    fun getGalleryImages() = callbackFlow {
        db.collection(Constants.GALLERY_DB_REF).addSnapshotListener { value, error ->
            if (error != null || value == null) {
                trySend(Resource.Error(error?.localizedMessage ?: "Could not get the images."))
            }
            if (value != null) {
                trySend(Resource.Success(value.toGalleryDataList()))
            }
        }

        awaitClose { cancel() }

    }
}