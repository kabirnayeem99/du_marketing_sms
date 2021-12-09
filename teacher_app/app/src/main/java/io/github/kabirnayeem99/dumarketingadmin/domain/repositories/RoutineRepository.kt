package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import io.github.kabirnayeem99.dumarketingadmin.data.model.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource

interface RoutineRepository {
    suspend fun insertRoutineData(routineData: RoutineData, batchYear: String): Resource<String>
}