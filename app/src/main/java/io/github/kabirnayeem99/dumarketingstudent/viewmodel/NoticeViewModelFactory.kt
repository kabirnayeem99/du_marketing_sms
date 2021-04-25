package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.NoticeRepository

class NoticeViewModelFactory(private val repository: NoticeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoticeViewModel(repository) as T
    }
}