package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import android.net.Uri
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.EbookDataSource
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.EbookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DefaultEbookRepository
@Inject constructor(
    private val dataSource: EbookDataSource
) : EbookRepository {

    override suspend fun uploadPdf(pdfFile: Uri, pdfName: String) = flow {
        emit(Resource.Loading())
        val uploadPdfResource = dataSource.uploadPdf(pdfFile, pdfName)
        emit(uploadPdfResource)
    }


    override suspend fun insertEbookDataToDb(ebookDto: EbookData) =
        dataSource.saveEbook(ebookDto)

    override suspend fun deleteEbookFromDb(ebookDto: EbookData) =
        dataSource.deleteEbook(ebookDto)

    override fun getEbooks(): Flow<Resource<List<EbookData>>> = dataSource.getEbooks()

}