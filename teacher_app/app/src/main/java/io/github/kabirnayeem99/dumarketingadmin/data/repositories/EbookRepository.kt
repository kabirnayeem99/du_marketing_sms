package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.github.kabirnayeem99.dumarketingadmin.data.db.BaasService
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData.Companion.toEbookDataList
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.EBOOK_DB_REF
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.EBOOK_STORAGE_PATH
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import javax.inject.Inject

class EbookRepository @Inject constructor(var db: FirebaseFirestore, var store: FirebaseStorage) {


    private val storage = store.reference.child(EBOOK_STORAGE_PATH)


    fun uploadPdf(pdfFile: Uri, pdfName: String): UploadTask {

        val filePath = storage.child("${System.currentTimeMillis()}-$pdfName")

        return filePath.putFile(pdfFile)

    }

    fun insertEbookDataToDb(ebookData: EbookData): Task<Void> {

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

        return db.collection(EBOOK_DB_REF).document(key).set(ebookData)
    }

    fun deleteEbookFromDb(ebookData: EbookData): Task<Void>? {

        Log.d(TAG, "deleteEbookFromDb: deleting $ebookData")

        ebookData.key?.let { key ->
            BaasService.storage.getReferenceFromUrl(ebookData.pdfUrl).delete().also {
                return db.collection(EBOOK_DB_REF).document(key).delete()
            }
        }

        return null
    }

    private val ebookLiveData = MutableLiveData<Resource<List<EbookData>>>()
    fun getEbooks(): LiveData<Resource<List<EbookData>>> {
        db.collection(EBOOK_DB_REF).addSnapshotListener(
            EventListener { value, e ->
                if (e != null) {

                    ebookLiveData.value =
                        Resource.Error(e.message ?: "Could not fetch from server.")

                    return@EventListener
                }

                value?.toEbookDataList().also { ebookDataList ->
                    ebookDataList?.let {
                        ebookLiveData.value = Resource.Success(it)
                    }
                }


            }
        )
        return ebookLiveData
    }

    companion object {
        private const val TAG = "EbookRepository"
    }
}