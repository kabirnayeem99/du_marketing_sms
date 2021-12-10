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
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData.Companion.toNoticeDataList
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.NOTICE_DB_REF
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.NOTICE_IMAGE_PATH_STRING_FOLDER_NAME
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import timber.log.Timber
import javax.inject.Inject

class NoticeRepository @Inject constructor(var db: FirebaseFirestore, var store: FirebaseStorage) {

    // initialise database and storage
    private val storage = store.reference.child(NOTICE_IMAGE_PATH_STRING_FOLDER_NAME)


    fun insertNoticeDataToDb(
        noticeData: NoticeData
    ): Task<Void> {

        lateinit var key: String

        if (noticeData.key != null) {
            noticeData.key?.let {
                key = it
            }
        } else {
            key = System.currentTimeMillis().toString().also {
                noticeData.key = it
            }
        }

        return db.collection(NOTICE_DB_REF)
            .document(key)
            .set(noticeData)
    }

    fun uploadNoticeImage(imageName: String, imageFile: ByteArray): UploadTask {

        val filePath = storage.child("$imageName.jpg")

        return filePath.putBytes(imageFile)

    }

    fun deleteNoticeData(noticeData: NoticeData): Task<Void>? {
        return noticeData.key?.let { key ->
            noticeData.imageUrl.let { url ->
                BaasService.storage.getReferenceFromUrl(url).delete()
            }.also {
                db.collection(NOTICE_DB_REF)
                    .document(key)
                    .delete()
            }

        }
    }

    private val noticeListLiveData = MutableLiveData<Resource<List<NoticeData>>>()

    fun getNoticeLiveData(): LiveData<Resource<List<NoticeData>>> {
        db.collection(NOTICE_DB_REF)
            .addSnapshotListener(
                EventListener { value, error ->

                    noticeListLiveData.value = Resource.Loading()

                    if (error != null) {
                        Timber.e("getNoticeLiveData: " + error.message)
                        error.printStackTrace()
                        noticeListLiveData.value =
                            Resource.Error("There was an error in the server")
                        return@EventListener
                    }

                    if (value != null) {
                        val noticeList = value.toNoticeDataList()
                        noticeListLiveData.value = Resource.Success(noticeList)
                    } else {
                        noticeListLiveData.value = Resource.Error("Empty List")
                    }
                }
            )

        return noticeListLiveData
    }

    companion object {
        private const val TAG = "AddNoticeRepository"
    }

}