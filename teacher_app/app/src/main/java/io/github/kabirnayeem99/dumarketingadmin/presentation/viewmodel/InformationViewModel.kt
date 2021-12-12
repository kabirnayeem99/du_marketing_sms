package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.InfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InformationViewModel @Inject constructor(
    private val repo: InfoRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    fun saveInformation(informationData: InformationData) {
        viewModelScope.launch {
            _isLoading.tryEmit(true)
            val res = repo.saveInformationData(informationData)
            _isLoading.emit(false)
            when (res) {
                is Resource.Error -> _errorMessage.tryEmit(res.message!!)
                is Resource.Success -> _message.tryEmit("Successfully saved the information.")
                else -> Unit
            }
        }
    }

    private val _informationData = MutableLiveData(InformationData())
    val informationData: LiveData<InformationData> = _informationData

    fun getInformationData() {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.emit(true)
            val res = repo.getCurrentInformation()
            _isLoading.emit(false)
            when (res) {
                is Resource.Error -> _errorMessage.emit(res.message!!)
                is Resource.Success -> {
                    _informationData.postValue(res.data!!)
                }
                else -> Unit
            }
        }
    }
}