package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.RoutineRepository

class RoutineViewModel(private val repo: RoutineRepository) : ViewModel() {

    fun getRoutine(batchYear: String) = repo.getRoutine(batchYear)
}