package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.InfoRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InformationViewModel @Inject constructor(
    private val repo: InfoRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    fun upsertInformation(informationData: InformationData) {
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            val res = repo.saveInformationData(informationData)
            _isLoading.postValue(false)
            when (res) {
                is Resource.Error -> _errorMessage.postValue(res.message)
                is Resource.Success -> _message.postValue("Successfully saved the information.")
                else -> Unit
            }
        }
    }

    private val _informationData = MutableLiveData(InformationData())
    val informationData: LiveData<InformationData> = _informationData

    fun getInformationData() {
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            val res = repo.getCurrentInformation()
            _isLoading.postValue(false)
            when (res) {
                is Resource.Error -> _errorMessage.postValue(res.message)
                is Resource.Success -> {
                    _informationData.postValue(res.data!!)
                }
                else -> Unit
            }
        }
    }
}