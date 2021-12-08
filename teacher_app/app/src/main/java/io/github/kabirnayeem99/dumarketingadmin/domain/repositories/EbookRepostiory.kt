package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.flow.Flow

interface EbookRepository {
    suspend fun uploadPdf(pdfFile: Uri, pdfName: String): Flow<Resource<String>>
    fun insertEbookDataToDb(ebookData: EbookData): Task<Void>
    fun deleteEbookFromDb(ebookData: EbookData): Task<Void>?
    fun getEbooks(): Flow<Resource<List<EbookData>>>
}