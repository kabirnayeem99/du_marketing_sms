package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.DefaultFacultyRepository
import io.github.kabirnayeem99.dumarketingadmin.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.FacultyRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FacultyViewModel @Inject constructor(
    val repo: FacultyRepository,
    val ioScope: CoroutineScope
) : BaseViewModel() {

    fun insertFacultyDataToDb(facultyData: FacultyData, post: String): Task<Void> =
        repo.upsertFacultyDataToDb(facultyData)


    fun uploadImage(imageFile: ByteArray, imageName: String): UploadTask {
        repo.uploadImage(imageFile, imageName)
    }

    fun deleteFacultyData(facultyData: FacultyData, post: String): Task<Void>? =
        repo.deleteFacultyData(facultyData)


    private val _facultyListObservable = MutableLiveData<List<FacultyData>>(emptyList())
    val facultyListObservable: LiveData<List<FacultyData>> = _facultyListObservable

    fun getFacultyList() {
        _isLoading.postValue(true)
        ioScope.launch {
            repo.getFacultyList().collect { resource ->
                _isLoading.postValue(false)
                when (resource) {
                    is Resource.Error -> _errorMessage.postValue(resource.message)
                    is Resource.Success -> _facultyListObservable.postValue(resource.data!!)
                    else -> Unit
                }
            }
        }
    }

}