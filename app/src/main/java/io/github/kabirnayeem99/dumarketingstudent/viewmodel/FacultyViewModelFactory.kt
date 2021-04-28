package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.FacultyRepository

class FacultyViewModelFactory(private val repository: FacultyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FacultyViewModel(repository) as T
    }
}