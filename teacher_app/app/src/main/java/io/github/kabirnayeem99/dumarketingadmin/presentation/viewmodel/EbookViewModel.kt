package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.model.EbookData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.EbookRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EbookViewModel @Inject constructor(
    val repo: EbookRepository,
    val ioContext: CoroutineDispatcher
) :
    BaseViewModel() {

    fun saveEbook(ebookData: EbookData) {
        viewModelScope.launch(ioContext) {
            _isLoading.postValue(true)
            when (val resource = repo.insertEbookDataToDb(ebookData)) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _message.postValue("Successfully uploaded ${resource.data}.")
                }
                is Resource.Error -> {
                    _isLoading.postValue(false)
                    _errorMessage.postValue(resource.message ?: "Something went wrong")
                }
                else -> _isLoading.postValue(false)
            }
        }
    }

    fun deleteEbook(ebookData: EbookData) {
        viewModelScope.launch(ioContext) {
            _isLoading.postValue(true)
            when (val resource = repo.deleteEbookFromDb(ebookData)) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _message.postValue("Successfully deleted ${resource.data}.")
                }
                is Resource.Error -> {
                    _isLoading.postValue(false)
                    _errorMessage.postValue(resource.message ?: "Something went wrong")
                }
                else -> _isLoading.postValue(false)
            }
        }
    }

    private val _ebookUrl = MutableLiveData<String>()
    val ebookUrl: LiveData<String> = _ebookUrl
    fun uploadPdf(pdfFile: Uri, pdfName: String) {
        viewModelScope.launch(ioContext) {
            repo.uploadPdf(pdfFile, pdfName).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _isLoading.postValue(false)
                        _ebookUrl.postValue(resource.data!!)
                        _message.postValue("Successfully uploaded ${resource.data}")
                    }
                    is Resource.Error -> {
                        _isLoading.postValue(false)
                        _errorMessage.postValue(resource.message ?: "Something went wrong")
                    }
                    is Resource.Loading -> _isLoading.postValue(true)
                }
            }
        }
    }

    private val _ebookList = MutableLiveData<List<EbookData>>()
    val ebookList: LiveData<List<EbookData>> = _ebookList
    fun getEbooks() {
        viewModelScope.launch(ioContext) {
            repo.getEbooks().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _isLoading.postValue(false)
                        _ebookList.postValue(resource.data!!)
                    }
                    is Resource.Error -> {
                        _isLoading.postValue(false)
                        _ebookList.postValue(emptyList())
                        _errorMessage.postValue(resource.message ?: "Something went wrong")
                    }
                    is Resource.Loading -> _isLoading.postValue(true)
                }
            }
        }
    }

}