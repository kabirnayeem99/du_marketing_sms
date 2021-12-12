package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.EbookRepository
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.OpenBookRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class EbookViewModel @Inject constructor(
    private val repo: EbookRepository,
    private val openBookRepo: OpenBookRepository,
    private val ioContext: CoroutineDispatcher
) :
    ViewModel() {


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveEbook(ebookData: EbookData) {
        viewModelScope.launch(ioContext) {
            _isLoading.postValue(true)
            when (val resource = repo.insertEbookDataToDb(ebookData)) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _message.postValue("Successfully saved ${resource.data}.")
                }
                is Resource.Error -> {
                    _isLoading.postValue(false)
                    _errorMessage.postValue(resource.message ?: "Something went wrong")
                }
                else -> _isLoading.postValue(false)
            }
        }
    }


    private val _recommendedBookList = MutableLiveData<List<EbookData>>()
    val recommendedBookList: LiveData<List<EbookData>> = _recommendedBookList
    fun searchBookDetails(query: String) {
        Timber.d("Searching for -> $query")
        viewModelScope.launch(ioContext) {
            openBookRepo.searchBooks(query).collect { resource ->
                when (resource) {
                    is Resource.Error -> _recommendedBookList.postValue(emptyList())
                    is Resource.Success -> _recommendedBookList.postValue(
                        resource.data ?: emptyList()
                    )
                    else -> Unit
                }
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
                        _message.postValue("Successfully uploaded ${pdfName}.\nYou can download it from ${resource.data}.")
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