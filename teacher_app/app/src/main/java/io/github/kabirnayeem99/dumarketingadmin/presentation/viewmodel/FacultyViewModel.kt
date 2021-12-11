package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.FacultyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FacultyViewModel @Inject constructor(
    private val repo: FacultyRepository,
    private val ioContext: CoroutineDispatcher,
) : BaseViewModel() {

    fun insertFacultyDataToDb(facultyData: FacultyData, post: String) {
        viewModelScope.launch(ioContext) {
            _isLoading.emit(true)
            val resource = repo.saveFacultyData(facultyData)
            _isLoading.emit(false)
            when (resource) {
                is Resource.Error -> _errorMessage.emit(resource.message ?: "")
                is Resource.Success -> _message.emit("${resource.data} is saved.")
                else -> Unit
            }
        }
    }


    suspend fun uploadImage(imageFile: ByteArray, imageName: String): String? {
        var url: String? = null
        _isLoading.emit(true)
        val resource = repo.uploadImage(imageFile, imageName)
        _isLoading.emit(false)
        when (resource) {
            is Resource.Error -> _errorMessage.emit("Image could not be uploaded")
            is Resource.Success -> {
                _message.emit("$imageName is successfully saved")
                url = resource.data
            }
            else -> Unit
        }
        return url
    }

    fun deleteFacultyData(facultyData: FacultyData, post: String) {
        viewModelScope.launch(ioContext) {
            _isLoading.emit(true)
            val resource = repo.deleteFacultyData(facultyData)
            _isLoading.emit(false)
            when (resource) {
                is Resource.Error -> _errorMessage.emit(resource.message ?: "")
                is Resource.Success -> _message.emit("Successfully deleted ${facultyData.name}.")
                else -> Unit
            }
        }
    }


    private val _facultyListObservable = MutableLiveData<List<FacultyData>>(emptyList())
    val facultyListObservable: LiveData<List<FacultyData>> = _facultyListObservable

    fun getFacultyList() {
        viewModelScope.launch(ioContext) {
            _isLoading.emit(true)
            repo.getFacultyList().collect { resource ->
                _isLoading.emit(false)
                when (resource) {
                    is Resource.Error -> _errorMessage.emit(resource.message!!)
                    is Resource.Success -> _facultyListObservable.postValue(resource.data!!)
                    else -> Unit
                }
            }
        }
    }

}