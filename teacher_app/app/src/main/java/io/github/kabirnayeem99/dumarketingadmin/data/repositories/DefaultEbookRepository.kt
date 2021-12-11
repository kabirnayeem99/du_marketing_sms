package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import android.net.Uri
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.EbookDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.model.EbookData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.EbookRepository
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
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