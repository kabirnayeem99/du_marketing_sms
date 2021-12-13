package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.mappers.BookMapper.toBookDataList
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EbookDataSource @Inject constructor(
    var db: FirebaseFirestore,
    store: FirebaseStorage
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
            Timber.e(e)
            return Resource.Error(e.localizedMessage ?: "$pdfName could not be uploaded")
        }
    }


    fun getEbooks() = callbackFlow {

        trySend(Resource.Loading())

        db.collection(Constants.EBOOK_DB_REF).addSnapshotListener { value, error ->
            if (error != null || value == null) {
                trySend(Resource.Error(error?.localizedMessage ?: "Could not fetch the books."))
            }

            if (value != null) {
                val ebookList = value.toBookDataList()
                Timber.d(ebookList.toString())
                trySend(Resource.Success(ebookList))
            }

        }

        awaitClose { cancel() }

    }


    suspend fun deleteEbook(ebookDto: EbookData): Resource<String> {

        try {
            BaasService.storage.getReferenceFromUrl(ebookDto.downloadUrl).delete().await()
        } catch (e: Exception) {
            Timber.e(e)
        }

        return try {
            ebookDto.id.let { key ->
                Timber.d("deleting $ebookDto with key ${ebookDto.id}")
                db.collection(Constants.EBOOK_DB_REF).document(key).delete().await()
            }
            Resource.Success(ebookDto.name)
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Error(e.localizedMessage ?: "Could not delete ${ebookDto.name}")
        } finally {
        }
    }

    suspend fun saveEbook(ebookDto: EbookData): Resource<String> {
        return try {
            var key: String = UUID.randomUUID().toString()
            if (ebookDto.id.isNotEmpty()) key = ebookDto.id
            db.collection(Constants.EBOOK_DB_REF).document(key).set(ebookDto).await()
            Resource.Success("${ebookDto.name}.")
        } catch (e: Exception) {
            Resource.Error("Could not save ${ebookDto.name}.")
        }
    }

}