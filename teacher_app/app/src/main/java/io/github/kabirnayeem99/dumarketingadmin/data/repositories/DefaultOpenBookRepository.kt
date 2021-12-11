package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.GoogleBooksDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.mappers.RecommendedBookMapper.toBookOpenBook
import io.github.kabirnayeem99.dumarketingadmin.domain.data.BookOpenBook
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.OpenBookRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DefaultOpenBookRepository
@Inject constructor(private val dataSource: GoogleBooksDataSource) :
    OpenBookRepository {
    override fun searchBooks(query: String) = flow<Resource<List<BookOpenBook>>> {
        try {
            val call = dataSource.searchBooks(query, 10).execute()
            if (!call.isSuccessful) {
                emit(Resource.Error("Could not get any data from remote."))
            } else {
                val bookList = mutableListOf<BookOpenBook>()
                call.body()?.items?.map {
                    bookList.add(it.volumeInfo.toBookOpenBook())
                }
                emit(Resource.Success(bookList))
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage ?: "searchBooks failed")
            emit(Resource.Error(e.localizedMessage ?: "Could not load the recommended message"))
        }

    }
}