package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.github.kabirnayeem99.dumarketingadmin.data.db.BaasService
import io.github.kabirnayeem99.dumarketingadmin.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.data.vo.FacultyData.Companion.toFacultyDataList
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.FACULTY_DB_COLLECTION_NAME
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.FACULTY_STORAGE_PATH
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import java.util.*
import javax.inject.Inject

class FacultyRepository @Inject constructor(var db: FirebaseFirestore, var store: FirebaseStorage) {

    // initialise database and storage
    private val storage = store.reference.child(FACULTY_STORAGE_PATH)


    // upload image
    fun uploadImage(imageFile: ByteArray, imageName: String): UploadTask {

        val filePath = storage.child("$imageName-$imageFile.jpg")

        return filePath.putBytes(imageFile)

    }

    // insert data to database
    fun upsertFacultyDataToDb(facultyData: FacultyData, post: String): Task<Void> {

        lateinit var key: String

        if (facultyData.key != null) {
            key = facultyData.key!!
        } else {
            key = facultyData.name.toLowerCase(Locale.ROOT).trimEnd() + System.currentTimeMillis()
            facultyData.key = key
        }

        return db.collection(FACULTY_DB_COLLECTION_NAME)
            .document(key)
            .set(facultyData)

    }

    // delete data
    fun deleteFacultyData(facultyData: FacultyData, post: String): Task<Void>? {
        return facultyData.key?.let { key ->
            facultyData.profileImageUrl?.let { url ->
                BaasService.storage.getReferenceFromUrl(url).delete()
            }.also {
                db.collection(FACULTY_DB_COLLECTION_NAME)
                    .document(key)
                    .delete()
            }

        }
    }

    // === chairman repo ===
    private val facultyListLiveData = MutableLiveData<Resource<List<FacultyData>>>()

    fun getFacultyListLiveData(): LiveData<Resource<List<FacultyData>>> {

        val ref = db.collection(FACULTY_DB_COLLECTION_NAME)

        ref.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    facultyListLiveData.value = Resource.Error(error.message ?: "Unknown error")
                    return@EventListener
                }

                val facultyList = value?.toFacultyDataList()

                Log.d(
                    TAG,
                    "getFacultyListLiveData: querySnapshot documents -> ${value?.documents ?: "Empty"}"
                )

                if (facultyList == null || facultyList.isEmpty()) {
                    facultyListLiveData.value = Resource.Error("Empty List")
                } else {
                    facultyListLiveData.value = Resource.Success(facultyList)
                    Log.d(TAG, "getFacultyListLiveData: $facultyList")
                }
            }
        )

        return facultyListLiveData
    }

    companion object {
        private const val TAG = "FacultyRepository"
    }

}