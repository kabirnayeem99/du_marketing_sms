package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.FacultyRepository

class FacultyViewModel(private val repo: FacultyRepository) : ViewModel() {

    fun getAllFacultyList() = repo.getAllFacultyList()
}