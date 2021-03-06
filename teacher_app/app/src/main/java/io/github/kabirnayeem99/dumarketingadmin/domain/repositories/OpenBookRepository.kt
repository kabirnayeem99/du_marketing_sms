package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import kotlinx.coroutines.flow.Flow

interface OpenBookRepository {
    fun searchBooks(query: String): Flow<Resource<List<EbookData>>>
}