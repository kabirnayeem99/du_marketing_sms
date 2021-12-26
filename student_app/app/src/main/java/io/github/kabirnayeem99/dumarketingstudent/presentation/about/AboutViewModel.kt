package io.github.kabirnayeem99.dumarketingstudent.presentation.about

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.AboutData
import io.github.kabirnayeem99.dumarketingstudent.domain.useCases.GetAboutDataUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel
@Inject constructor(
    private val getAboutDataUseCase: GetAboutDataUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    init {
        getAboutData()
    }

    private var fetchAboutDataJob: Job? = null
    private fun getAboutData() {
        fetchAboutDataJob?.cancel()
        fetchAboutDataJob = viewModelScope.launch {
            getAboutDataUseCase.invoke().collect { resource ->
                _uiState.update {
                    it.copy(
                        message = resource.message ?: "",
                        isLoading = resource.data == null,
                        about = resource.data ?: AboutData()
                    )
                }
            }
        }
    }


}
