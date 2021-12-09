package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.DefaultRoutineRepository
import io.github.kabirnayeem99.dumarketingadmin.data.vo.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class RoutineViewModel @Inject constructor(
    val repo: DefaultRoutineRepository,
    val ioContext: CoroutineDispatcher
) : BaseViewModel() {


    var batchYear: String = ""
        set(value) {
            if (value.isNotEmpty()) field = value
        }

    var selectedRoutine: RoutineData? = null
        set(value) {
            if (value != null) {
                if (value.time.isNotEmpty() && value.className.isNotEmpty()) field = value
            }
        }

    private val _routines = MutableLiveData<List<RoutineData>>()
    val routines: LiveData<List<RoutineData>> = _routines

    @ExperimentalCoroutinesApi
    fun getRoutine(batchYear: String) {
        _isLoading.postValue(true)
        viewModelScope.launch(ioContext) {
            repo.getRoutine(batchYear).collect { resource ->
                _isLoading.postValue(false)
                when (resource) {
                    is Resource.Error -> _errorMessage.postValue(resource.message)
                    is Resource.Success -> _routines.postValue(resource.data ?: emptyList())
                    else -> Unit
                }
            }
        }
    }

    fun insertRoutineData(routineData: RoutineData, batchYear: String) {
        _isLoading.postValue(true)
        viewModelScope.launch(ioContext) {
            val resource = repo.insertRoutineData(routineData, batchYear)
            _isLoading.postValue(false)
            when (resource) {
                is Resource.Error -> _errorMessage.postValue(resource.message)
                is Resource.Success -> _message.postValue("Saved successfully.")
                else -> Unit
            }
        }
    }
}