package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData.Companion.toFacultyDataList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FacultyDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    store: FirebaseStorage,
) {
    private val storage = store.reference.child(Constants.FACULTY_STORAGE_PATH)

    suspend fun uploadImageToRemote(imageFile: ByteArray, imageName: String): Resource<String> {
        return try {
            val imagePath = storage.child("$imageName-$imageFile.jpg")
            val imageUploadTask = imagePath.putBytes(imageFile).await()
            val url = imageUploadTask?.metadata?.reference?.downloadUrl?.await()
            if (url != null) Resource.Success(url.toString())
            else Resource.Error("Could not upload the image.")
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not upload the image.")
        }
    }

    // insert data to database
    suspend fun saveFacultyDataToRemote(facultyData: FacultyData): Resource<String> {
        try {
            lateinit var key: String

            if (facultyData.key != null) {
                key = facultyData.key!!
            } else {
                key = facultyData.name.lowercase(Locale.ROOT).trimEnd() + System.currentTimeMillis()
                facultyData.key = key
            }

            db.collection(Constants.FACULTY_DB_COLLECTION_NAME)
                .document(key)
                .set(facultyData).await()

            return Resource.Success(key)
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: "")
        }
    }

    // delete data
    suspend fun deleteFacultyDataFromRemote(facultyData: FacultyData): Resource<String> {

        try {
            facultyData.profileImageUrl?.let { url ->
                BaasService.storage.getReferenceFromUrl(url).delete().await()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return try {
            val facultyDataKey = facultyData.key ?: ""
            db.collection(Constants.FACULTY_DB_COLLECTION_NAME)
                .document(facultyDataKey)
                .delete().await()
            Resource.Success(facultyData.email)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not delete faculty data.")
        }
    }


    @ExperimentalCoroutinesApi
    fun getFacultyList() = callbackFlow {
        val ref = db.collection(Constants.FACULTY_DB_COLLECTION_NAME)
        try {
            ref.addSnapshotListener { value, error ->
                if (error != null || value == null)
                    trySend(Resource.Error(error?.localizedMessage ?: ""))
                if (value != null)
                    trySend(Resource.Success(value.toFacultyDataList()))
            }
        } catch (e: Exception) {
            trySend(Resource.Error(e.localizedMessage ?: ""))
        }

        awaitClose { cancel() }
    }

}