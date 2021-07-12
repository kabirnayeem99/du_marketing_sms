package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.AboutRepository
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(var repo: AboutRepository) : ViewModel() {
    fun getAboutData() = repo.getAboutData()
}
