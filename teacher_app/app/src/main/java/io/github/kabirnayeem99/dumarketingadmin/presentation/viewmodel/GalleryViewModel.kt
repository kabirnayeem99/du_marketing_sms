package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.DefaultGalleryRepository
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.GalleryRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GalleryViewModel @Inject constructor(
    val repo: GalleryRepository,
    val ioDispatcher: CoroutineDispatcher,
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
    }

    suspend fun deleteGalleryData(galleryData: GalleryData) {
        repo.deleteGalleryData(galleryData)
    }

    fun getGalleryDataList(): LiveData<Resource<List<GalleryData>>> =
        repo.getGalleryDataListObservable()
}