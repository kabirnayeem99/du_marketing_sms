package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.DefaultNoticeRepository
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val repo: DefaultNoticeRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    fun saveNotice(noticeData: NoticeData) {
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            val resource = repo.saveNotice(noticeData)
            _isLoading.postValue(false)
            when (resource) {
                is Resource.Error -> _errorMessage.postValue(resource.message)
                is Resource.Success -> _message.postValue("Successfully saved ${resource.data}.")
                else -> Unit
            }
        }
    }

    fun deleteNoticeDataToDb(noticeData: NoticeData) {
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            val resource = repo.deleteNoticeData(noticeData)
            _isLoading.postValue(false)
            when (resource) {
                is Resource.Error -> _errorMessage.postValue(resource.message)
                is Resource.Success -> _message.postValue("Successfully deleted ${resource.data}.")
                else -> Unit
            }
        }
    }


    suspend fun uploadNoticeImage(imageName: String, imageFile: ByteArray): String? {
        var url: String? = null
        _isLoading.postValue(true)
        val resource = repo.uploadNoticeImage(imageName, imageFile)
        _isLoading.postValue(false)
        when (resource) {
            is Resource.Error -> _errorMessage.postValue("Could not upload $imageName.")
            is Resource.Success -> {
                url = resource.data
                _message.postValue("Successfully uploaded the image.")
            }
            else -> Unit
        }

        return url
    }

    private val _notices = MutableStateFlow(emptyList<NoticeData>())
    val notices = _notices.asLiveData()

    fun getNoticeList() {
        _isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            repo.getNoticeList().collect { resource ->
                _isLoading.postValue(false)
                when (resource) {
                    is Resource.Error -> _errorMessage.postValue(resource.message)
                    is Resource.Success -> _notices.value = resource.data ?: emptyList()
                    else -> Unit
                }
            }
        }
    }
}