package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.FacultyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class FacultyViewModel @Inject constructor(
    private val repo: FacultyRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _shouldLeavePage = MutableLiveData<Boolean>(false)
    val shouldLeavePage: LiveData<Boolean> = _shouldLeavePage
    fun resetLeavePageStatus() {
        _shouldLeavePage.postValue(false)
    }

    fun saveFacultyData(facultyData: FacultyData, post: String) {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.postValue(true)
            val resource = repo.saveFacultyData(facultyData)
            _isLoading.postValue(false)
            when (resource) {
                is Resource.Error -> _errorMessage.postValue(resource.message ?: "")
                is Resource.Success -> _message.postValue("${resource.data} is saved.")
                else -> Unit
            }
            _shouldLeavePage.postValue(true)
        }
    }


    suspend fun uploadImage(imageFile: ByteArray, imageName: String): String? {
        var url: String? = null
        _isLoading.postValue(true)
        val resource = repo.uploadImage(imageFile, imageName)
        _isLoading.postValue(false)
        when (resource) {
            is Resource.Error -> _errorMessage.postValue("Image could not be uploaded")
            is Resource.Success -> {
                _message.postValue("$imageName is successfully saved")
                url = resource.data
            }
            else -> Unit
        }
        return url
    }

    fun deleteFacultyData(facultyData: FacultyData, post: String) {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.postValue(true)
            val resource = repo.deleteFacultyData(facultyData)
            _isLoading.postValue(false)
            when (resource) {
                is Resource.Error -> _errorMessage.postValue(resource.message ?: "")
                is Resource.Success -> _message.postValue("Successfully deleted ${facultyData.name}.")
                else -> Unit
            }
            _shouldLeavePage.postValue(true)
        }
    }


    private val _facultyListObservable = MutableLiveData<List<FacultyData>>(emptyList())
    val facultyListObservable: LiveData<List<FacultyData>> = _facultyListObservable

    fun getFacultyList() {
        viewModelScope.launch(ioDispatcher) {
            _isLoading.postValue(true)
            repo.getFacultyList().collect { resource ->
                _isLoading.postValue(false)
                when (resource) {
                    is Resource.Error -> _errorMessage.postValue(resource.message!!)
                    is Resource.Success -> _facultyListObservable.postValue(resource.data!!)
                    else -> Unit
                }
            }
        }
    }

}