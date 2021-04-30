package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.AboutRepository

class AboutViewModelFactory(private val repository: AboutRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AboutViewModel(repository) as T
    }
}