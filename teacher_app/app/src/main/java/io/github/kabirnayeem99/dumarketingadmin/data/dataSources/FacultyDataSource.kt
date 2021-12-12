package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData.Companion.toFacultyDataList
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class FacultyDataSource @Inject constructor(
    val db: FirebaseFirestore,
    val store: FirebaseStorage,
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
            return Resource.Error(e.localizedMessage ?: "Could not save the faculty data.")
        }
    }

    // delete data
    suspend fun deleteFacultyDataFromRemote(facultyData: FacultyData): Resource<String> {
        return try {
            facultyData.key?.let { key ->
                facultyData.profileImageUrl?.let { url ->
                    BaasService.storage.getReferenceFromUrl(url).delete().await()
                }.also {
                    db.collection(Constants.FACULTY_DB_COLLECTION_NAME)
                        .document(key)
                        .delete().await()
                }

            }
            Resource.Success(facultyData.email)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not delete faculty data.")
        }
    }


    @ExperimentalCoroutinesApi
    fun getFacultyList() = callbackFlow<Resource<List<FacultyData>>> {
        val ref = db.collection(Constants.FACULTY_DB_COLLECTION_NAME)
        try {
            ref.addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    trySend(
                        Resource.Error(error?.localizedMessage ?: "Could not get the faculty list.")
                    )
                }
                if (value != null) {
                    trySend(Resource.Success(value.toFacultyDataList()))
                }
            }
        } catch (e: Exception) {
            trySend(Resource.Error(e.localizedMessage ?: "Could not get the faculty list."))
        }

        awaitClose { cancel() }
    }

}