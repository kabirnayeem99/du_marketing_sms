package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.AboutRepository

class AboutViewModel(private val repo: AboutRepository) : ViewModel() {
    fun getAboutIntro() = repo.getAboutIntro()
    fun getMailAddress() = repo.getMailAddress()
    fun getTelephoneNumber() = repo.getTelephoneNumber()
    fun getLocation() = repo.getLocation()
}
