package io.github.kabirnayeem99.dumarketingadmin.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    protected val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    protected val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    protected val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
}