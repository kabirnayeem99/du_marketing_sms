package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.RoutineRepository
import io.github.kabirnayeem99.dumarketingadmin.data.vo.RoutineData
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(val repo: RoutineRepository) : ViewModel() {

    fun getRoutine(batchYear: String) = repo.getRoutine(batchYear)
    fun insertRoutineData(routineData: RoutineData, batchYear: String) =
        repo.insertRoutineData(routineData, batchYear)
}