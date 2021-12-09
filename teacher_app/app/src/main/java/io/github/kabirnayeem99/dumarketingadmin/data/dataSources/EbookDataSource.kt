package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.github.kabirnayeem99.dumarketingadmin.data.model.EbookData
import io.github.kabirnayeem99.dumarketingadmin.data.model.EbookData.Companion.toEbookDataList
import io.github.kabirnayeem99.dumarketingadmin.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EbookDataSource @Inject constructor(
    var db: FirebaseFirestore,
    var store: FirebaseStorage
) {

    private val storage = store.reference.child(Constants.EBOOK_STORAGE_PATH)

    suspend fun uploadPdf(pdfFile: Uri, pdfName: String): Resource<String> {

        try {
            val filePath = storage.child("${System.currentTimeMillis()}-$pdfName")

            val uploadTask = filePath.putFile(pdfFile).await()

            return if (uploadTask.error != null) {
                Resource.Error(
                    uploadTask.error?.localizedMessage ?: "$pdfName could not be uploaded"
                )
            } else {
                val url: String = uploadTask.metadata?.reference?.downloadUrl?.await().toString()
                Resource.Success(url)
            }
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: "$pdfName could not be uploaded")
        }
    }


    fun getEbooks(): Flow<Resource<List<EbookData>>> = callbackFlow<Resource<List<EbookData>>> {

        trySend(Resource.Loading())

        db.collection(Constants.EBOOK_DB_REF).addSnapshotListener { value, error ->
            if (error != null || value == null) {
                trySend(Resource.Error(error?.localizedMessage ?: "Could not fetch the books."))
            }

            if (value != null) {
                val ebookList = value.toEbookDataList()
                trySend(Resource.Success(ebookList))
            }

        }

        awaitClose { cancel() }

    }


    suspend fun deleteEbookFromDb(ebookData: EbookData): Resource<String> {
        return try {
            ebookData.key?.let { key ->
                BaasService.storage.getReferenceFromUrl(ebookData.pdfUrl).delete().also {
                    db.collection(Constants.EBOOK_DB_REF).document(key).delete().await()
                }
            }?.await()
            Resource.Success("Successfully delete ${ebookData.title}")
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not delete ${ebookData.title}")
        }
    }

    suspend fun insertEbookDataToDb(ebookData: EbookData): Resource<String> {
        try {
            lateinit var key: String
            if (ebookData.key == null) {
                key = ebookData.title.replace("\\s".toRegex(), "").also {
                    ebookData.key = it
                }
            } else {
                ebookData.key?.let {
                    key = it
                }
            }
            db.collection(Constants.EBOOK_DB_REF).document(key).set(ebookData).await()
            return Resource.Success("Successfully saved ${ebookData.title}.")
        } catch (e: Exception) {
            return Resource.Error("Could not save ${ebookData.title}.")
        }
    }

}