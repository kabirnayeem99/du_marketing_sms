package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.EbookRepository

class EbookViewModel(private val repo: EbookRepository) : ViewModel() {
    fun getEbooks() = repo.getEbooks()
}
