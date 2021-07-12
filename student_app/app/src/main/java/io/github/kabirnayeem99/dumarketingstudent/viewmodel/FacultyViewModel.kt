package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.FacultyRepository
import io.github.kabirnayeem99.dumarketingstudent.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import javax.inject.Inject

@HiltViewModel
class FacultyViewModel @Inject constructor(var repo: FacultyRepository) : ViewModel() {

    fun getAllFacultyList(): MutableLiveData<Resource<List<FacultyData>>> = repo.getAllFacultyList()
}