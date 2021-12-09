package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepo: AuthenticationRepository,
    private val ioContext: CoroutineDispatcher,
) : BaseViewModel() {


    private val _authenticated = MutableLiveData<Boolean>()
    val authenticated: LiveData<Boolean> = _authenticated
    fun getAuthenticationStatus() = viewModelScope.launch(ioContext) {
        _isLoading.postValue(true)
        authRepo.getAuthenticationStatus().collect { authenticationStatus ->
            _isLoading.postValue(false)
            if (authenticationStatus) _message.postValue("Successfully logged in.")
            _authenticated.postValue(authenticationStatus)
        }
    }

    fun logOut() {
        authRepo.logOut()
    }
}