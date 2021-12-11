package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.GalleryRepository
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
        viewModelScope.launch(ioDispatcher) {
            _isLoading.emit(true)
            val resource = repo.saveGalleryData(galleryData)
            _isLoading.emit(false)
            when (resource) {
                is Resource.Error -> _errorMessage.emit(resource.message!!)
                is Resource.Success -> _message.emit("Successfully saved ${resource.data}")
                else -> Unit
            }
        }
    }


    fun startLoading() {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.emit(true)
        }
    }

    fun stopLoading() {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.emit(false)
        }
    }

    suspend fun uploadGalleryImage(
        category: String,
        imageFile: ByteArray
    ): String? {
        var url: String? = null
        viewModelScope.launch(ioDispatcher) {
            _isLoading.emit(true)
            val resource = repo.uploadGalleryImage(category, imageFile)
            _isLoading.emit(false)
            when (resource) {
                is Resource.Error -> _errorMessage.emit(resource.message!!)
                is Resource.Success -> {
                    _message.emit("Successfully saved the image in $category.")
                    url = resource.data
                }
                else -> Unit
            }

        }
        return url
    }

    fun deleteGalleryData(galleryData: GalleryData) {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.emit(true)
            val resource = repo.deleteGalleryData(galleryData)
            _isLoading.emit(false)
            when (resource) {
                is Resource.Error -> _errorMessage.emit(resource.message!!)
                is Resource.Success -> _message.emit("${galleryData.category} is deleted")
                else -> Unit
            }
        }
    }


    private val _galleryDataList = MutableStateFlow<List<GalleryData>>(emptyList())
    val galleryDataList = _galleryDataList.asLiveData()
    fun getGalleryDataList() {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.emit(true)
            repo.getGalleryImages().collect { resource ->
                _isLoading.emit(false)
                when (resource) {
                    is Resource.Error -> _errorMessage.emit(resource.message!!)
                    is Resource.Success -> _galleryDataList.value = resource.data ?: emptyList()
                    else -> Unit
                }
            }
        }
    }
}