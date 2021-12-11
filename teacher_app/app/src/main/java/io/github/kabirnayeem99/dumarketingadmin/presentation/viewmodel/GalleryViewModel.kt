package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.GalleryRepository
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repo: GalleryRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    fun saveGalleryData(
        galleryData: GalleryData
    ) {
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            val resource = repo.saveGalleryData(galleryData)
            _isLoading.postValue(false)
            when (resource) {
                is Resource.Error -> _errorMessage.postValue(resource.message)
                is Resource.Success -> _message.postValue("Successfully saved ${resource.data}")
                else -> Unit
            }
        }
    }


    fun startLoading() {
        _isLoading.value = true
    }

    fun stopLoading() {
        _isLoading.value = false
    }

    suspend fun uploadGalleryImage(
        category: String,
        imageFile: ByteArray
    ): String? {
        var url: String? = null
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            val resource = repo.uploadGalleryImage(category, imageFile)
            _isLoading.postValue(false)
            when (resource) {
                is Resource.Error -> _errorMessage.postValue(resource.message)
                is Resource.Success -> {
                    _message.postValue("Successfully saved the image in $category.")
                    url = resource.data
                }
                else -> Unit
            }

        }
        return url
    }

    fun deleteGalleryData(galleryData: GalleryData) {
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            val resource = repo.deleteGalleryData(galleryData)
            _isLoading.postValue(false)
            when (resource) {
                is Resource.Error -> _errorMessage.postValue(resource.message)
                is Resource.Success -> _message.postValue("${galleryData.category} is deleted")
                else -> Unit
            }
        }
    }


    private val _galleryDataList = MutableStateFlow<List<GalleryData>>(emptyList())
    val galleryDataList = _galleryDataList.asLiveData()
    fun getGalleryDataList() {
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            repo.getGalleryImages().collect { resource ->
                _isLoading.postValue(false)
                when (resource) {
                    is Resource.Error -> _errorMessage.postValue(resource.message)
                    is Resource.Success -> _galleryDataList.value = resource.data ?: emptyList()
                    else -> Unit
                }
            }
        }
    }
}