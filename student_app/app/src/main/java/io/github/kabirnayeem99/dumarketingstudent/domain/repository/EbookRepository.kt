package io.github.kabirnayeem99.dumarketingstudent.domain.repository

import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.EbookData
import kotlinx.coroutines.flow.Flow

interface EbookRepository {
    fun getEbooks(): Flow<Resource<List<EbookData>>>
}