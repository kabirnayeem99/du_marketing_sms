package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.InfoDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.InfoRepository
import javax.inject.Inject

class DefaultInfoRepository @Inject constructor(private var dataSource: InfoDataSource) : InfoRepository {
    override suspend fun saveInformationData(informationData: InformationData) =
        dataSource.saveInformationData(informationData)

    override suspend fun getCurrentInformation() = dataSource.getInformationData()

}