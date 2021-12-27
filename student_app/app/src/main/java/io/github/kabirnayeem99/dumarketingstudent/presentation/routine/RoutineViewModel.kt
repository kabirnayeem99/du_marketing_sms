package io.github.kabirnayeem99.dumarketingstudent.presentation.routine

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.RoutineRepository
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(var repo: RoutineRepository) : ViewModel() {

    fun getRoutine(batchYear: String) = repo.getRoutine(batchYear)
}