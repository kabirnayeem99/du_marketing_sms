package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import android.net.Uri
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData.Companion.toEbookDataList
import io.github.kabirnayeem99.dumarketingadmin.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
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

    }

}