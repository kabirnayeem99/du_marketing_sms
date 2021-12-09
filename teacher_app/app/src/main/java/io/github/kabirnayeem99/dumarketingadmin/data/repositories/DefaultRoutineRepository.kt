package io.github.kabirnayeem99.dumarketingadmin.data.repositories


import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.RoutineDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.model.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.RoutineRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class DefaultRoutineRepository @Inject constructor(val dataSource: RoutineDataSource) :
    RoutineRepository {

    override suspend fun insertRoutineData(
        routineData: RoutineData,
        batchYear: String
    ): Resource<String> = dataSource.insertRoutineData(routineData, batchYear)


    @ExperimentalCoroutinesApi
    fun getRoutine(batchYear: String) = dataSource.getRoutine(batchYear)

}