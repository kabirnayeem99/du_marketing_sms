package io.github.kabirnayeem99.dumarketingstudent.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.FacultyRepository
import io.github.kabirnayeem99.dumarketingstudent.data.dto.FacultyData
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import javax.inject.Inject

@HiltViewModel
class FacultyViewModel @Inject constructor(var repo: FacultyRepository) : ViewModel() {

    fun getAllFacultyList(): MutableLiveData<Resource<List<FacultyData>>> = repo.getAllFacultyList()
}