package io.github.kabirnayeem99.dumarketingadmin.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

open class BaseViewModel : ViewModel() {

    protected val _errorMessage = MutableSharedFlow<String>(replay = 1)
    val errorMessage = _errorMessage.asSharedFlow()

    protected val _message = MutableSharedFlow<String>(replay = 1)
    val message = _message.asSharedFlow()

    protected val _isLoading = MutableSharedFlow<Boolean>(replay = 1)
    val isLoading = _isLoading.asSharedFlow()
}