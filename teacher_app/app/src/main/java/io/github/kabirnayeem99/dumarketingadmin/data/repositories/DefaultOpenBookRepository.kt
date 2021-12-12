package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.GoogleBooksDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.mappers.BookMapper.toBookData
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.OpenBookRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DefaultOpenBookRepository
@Inject constructor(private val dataSource: GoogleBooksDataSource) :
    OpenBookRepository {
    override fun searchBooks(query: String) = flow<Resource<List<EbookData>>> {
        try {
            val call = dataSource.searchBooks(query, 10).execute()
            Timber.d("THe call response is ${call.raw().body}")
            if (!call.isSuccessful) {
                Timber.e(call.errorBody().toString())
                emit(Resource.Error("Could not get any data from remote."))
            } else {
                val bookList = mutableListOf<EbookData>()
                call.body()?.items?.map {
                    bookList.add(it.volumeInfo.toBookData())
                }
                emit(Resource.Success(bookList))
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage ?: "searchBooks failed")
            emit(Resource.Error(e.localizedMessage ?: "Could not load the recommended message"))
        }

    }
}