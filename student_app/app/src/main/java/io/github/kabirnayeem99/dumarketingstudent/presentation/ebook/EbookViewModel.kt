package io.github.kabirnayeem99.dumarketingstudent.presentation.ebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.domain.useCases.GetEbooksUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EbookViewModel @Inject constructor(private val getEbooksUseCase: GetEbooksUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(EbookUiState())
    val uiState = _uiState.asStateFlow()


    private var fetchEbookJob: Job? = null
    fun fetchEbooks() {
        fetchEbookJob?.cancel()
        fetchEbookJob = viewModelScope.launch {
            getEbooksUseCase.getEbookFlow().collect { resource ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = resource.message ?: "",
                        ebookList = resource.data ?: emptyList(),
                    )
                }
            }
        }
    }
}
