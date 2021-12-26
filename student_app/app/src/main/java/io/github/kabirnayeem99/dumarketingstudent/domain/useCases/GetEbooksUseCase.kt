package io.github.kabirnayeem99.dumarketingstudent.domain.useCases

import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.EbookData
import io.github.kabirnayeem99.dumarketingstudent.domain.repository.EbookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEbooksUseCase @Inject constructor(val repo: EbookRepository) {

    fun getEbookFlow(): Flow<Resource<List<EbookData>>> {
        return repo.getEbooks()
    }

}