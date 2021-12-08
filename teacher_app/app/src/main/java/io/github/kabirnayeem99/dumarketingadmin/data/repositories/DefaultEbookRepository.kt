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
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.BaasService
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.EbookDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData.Companion.toEbookDataList
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.EbookRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.EBOOK_DB_REF
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.EBOOK_STORAGE_PATH
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DefaultEbookRepository
@Inject constructor(
    val dataSource: EbookDataSource
) : EbookRepository {

    override suspend fun uploadPdf(pdfFile: Uri, pdfName: String) = flow {
        emit(Resource.Loading())
        val uploadPdfResource = dataSource.uploadPdf(pdfFile, pdfName)
        emit(uploadPdfResource)
    }


    override suspend fun insertEbookDataToDb(ebookData: EbookData) =
        dataSource.insertEbookDataToDb(ebookData)

    override suspend fun deleteEbookFromDb(ebookData: EbookData) =
        dataSource.deleteEbookFromDb(ebookData)

    @ExperimentalCoroutinesApi
    override fun getEbooks(): Flow<Resource<List<EbookData>>> = dataSource.getEbooks()

}