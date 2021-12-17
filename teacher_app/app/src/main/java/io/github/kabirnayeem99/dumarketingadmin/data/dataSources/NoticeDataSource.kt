package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData.Companion.toNoticeDataList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoticeDataSource @Inject constructor(
    private var db: FirebaseFirestore,
    store: FirebaseStorage,
) {
    // initialise database and storage
    private val storage = store.reference.child(Constants.NOTICE_IMAGE_PATH_STRING_FOLDER_NAME)


    suspend fun saveNotice(
        noticeData: NoticeData
    ): Resource<String> {

        try {
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

            db.collection(Constants.NOTICE_DB_REF)
                .document(key)
                .set(noticeData).await()

            return Resource.Success(noticeData.title)
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: "Could not save the notice.")
        }

    }

    suspend fun uploadNoticeImage(imageName: String, imageFile: ByteArray): Resource<String> {

        return try {
            val filePath = storage.child("$imageName.jpg")

            val uri = filePath.putBytes(imageFile).await().storage.downloadUrl

            Resource.Success(uri.toString())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not upload $imageName.")
        }

    }

    suspend fun deleteNoticeData(noticeData: NoticeData): Resource<String> {
        return try {
            noticeData.key?.let { key ->
                noticeData.imageUrl.let { url ->
                    BaasService.storage.getReferenceFromUrl(url).delete().await()
                }.also {
                    db.collection(Constants.NOTICE_DB_REF)
                        .document(key)
                        .delete().await()
                }
            }
            Resource.Success(noticeData.title)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not delete ${noticeData.title}.")
        }
    }


    @ExperimentalCoroutinesApi
    fun getNoticeList() = callbackFlow {
        db.collection(Constants.NOTICE_DB_REF).addSnapshotListener { value, error ->
            if (value == null || error != null) {
                trySend(Resource.Error(error?.localizedMessage ?: "Could not get the notice list."))
            }

            if (value != null) {
                trySend(Resource.Success(value.toNoticeDataList()))
            }
        }

        awaitClose { close() }
    }

}