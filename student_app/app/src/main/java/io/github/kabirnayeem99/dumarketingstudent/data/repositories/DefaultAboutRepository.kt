package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.data.dataSources.AboutRemoteDataSource
import io.github.kabirnayeem99.dumarketingstudent.data.dto.AboutData
import io.github.kabirnayeem99.dumarketingstudent.domain.repository.AboutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultAboutRepository @Inject constructor(private val dataSource: AboutRemoteDataSource) :
    AboutRepository {

    override fun getAboutData(): Flow<Resource<AboutData>> = dataSource.getAboutData()
}