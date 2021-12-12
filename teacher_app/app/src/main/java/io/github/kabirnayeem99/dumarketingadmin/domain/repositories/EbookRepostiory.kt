package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import android.net.Uri
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import kotlinx.coroutines.flow.Flow

interface EbookRepository {
    suspend fun uploadPdf(pdfFile: Uri, pdfName: String): Flow<Resource<String>>
    suspend fun insertEbookDataToDb(ebookDto: EbookData): Resource<String>
    suspend fun deleteEbookFromDb(ebookDto: EbookData): Resource<String>
    fun getEbooks(): Flow<Resource<List<EbookData>>>
}