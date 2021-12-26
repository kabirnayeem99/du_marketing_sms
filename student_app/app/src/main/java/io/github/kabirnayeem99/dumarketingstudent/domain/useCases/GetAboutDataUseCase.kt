package io.github.kabirnayeem99.dumarketingstudent.domain.useCases

import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.AboutData
import io.github.kabirnayeem99.dumarketingstudent.domain.repository.AboutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAboutDataUseCase @Inject constructor(private val repo: AboutRepository) {
    suspend operator fun invoke(): Flow<Resource<AboutData>> {
        return repo.getAboutData()
    }
}