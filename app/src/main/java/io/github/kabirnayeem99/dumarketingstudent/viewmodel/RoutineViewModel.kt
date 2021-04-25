package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.RoutineRepository
import io.github.kabirnayeem99.dumarketingstudent.data.vo.RoutineData

class RoutineViewModel(private val repo: RoutineRepository) : ViewModel() {

    fun getRoutine(): LiveData<RoutineData> = repo.getRoutine()
}