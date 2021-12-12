package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.DefaultRoutineRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        viewModelScope.launch(ioContext) {
            _isLoading.emit(true)
            repo.getRoutine(batchYear).collect { resource ->
                _isLoading.emit(false)
                when (resource) {
                    is Resource.Error -> _errorMessage.emit(resource.message!!)
                    is Resource.Success -> _routines.postValue(resource.data ?: emptyList())
                    else -> Unit
                }
            }
        }
    }

    fun insertRoutineData(routineData: RoutineData, batchYear: String) {
        viewModelScope.launch(ioContext) {
            _isLoading.emit(true)
            val resource = repo.insertRoutineData(routineData, batchYear)
            _isLoading.emit(false)
            when (resource) {
                is Resource.Error -> _errorMessage.emit(resource.message!!)
                is Resource.Success -> _message.emit("Saved successfully.")
                else -> Unit
            }
        }
    }
}