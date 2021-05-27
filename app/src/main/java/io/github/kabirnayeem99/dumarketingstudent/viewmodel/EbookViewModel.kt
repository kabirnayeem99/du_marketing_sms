package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.EbookRepository
import javax.inject.Inject

@HiltViewModel
class EbookViewModel @Inject constructor(var repo: EbookRepository) : ViewModel() {
    fun getEbooks() = repo.getEbooks()
}
