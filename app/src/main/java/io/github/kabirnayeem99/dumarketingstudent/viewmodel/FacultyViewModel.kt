package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.FacultyRepository
import io.github.kabirnayeem99.dumarketingstudent.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingstudent.util.Resource

class FacultyViewModel(private val repo: FacultyRepository) : ViewModel() {

    fun getAllFacultyList(): MutableLiveData<Resource<List<FacultyData>>> = repo.getAllFacultyList()
}