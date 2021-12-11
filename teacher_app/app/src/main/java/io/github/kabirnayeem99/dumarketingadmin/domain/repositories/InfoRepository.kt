package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource

interface InfoRepository {
    suspend fun saveInformationData(informationData: InformationData): Resource<String>
    suspend fun getCurrentInformation(): Resource<InformationData>
}