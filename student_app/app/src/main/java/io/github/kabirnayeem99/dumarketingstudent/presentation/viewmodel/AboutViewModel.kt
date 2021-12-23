package io.github.kabirnayeem99.dumarketingstudent.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.DefaultAboutRepository
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(var repo: DefaultAboutRepository) : ViewModel() {
    fun getAboutData() = repo.getAboutData()
}
