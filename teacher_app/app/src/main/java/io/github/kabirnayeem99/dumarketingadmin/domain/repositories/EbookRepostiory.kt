package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import android.net.Uri
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.flow.Flow

interface EbookRepository {
    suspend fun uploadPdf(pdfFile: Uri, pdfName: String): Flow<Resource<String>>
    suspend fun insertEbookDataToDb(ebookData: EbookData): Resource<String>
    suspend fun deleteEbookFromDb(ebookData: EbookData): Resource<String>
    fun getEbooks(): Flow<Resource<List<EbookData>>>
}