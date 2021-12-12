package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthenticationRepository,
    private val ioContext: CoroutineDispatcher,
) : BaseViewModel() {

    var email: String = ""
    var password: String = ""

    private val _loginSuccess = MutableLiveData<String>()
    val loginSuccess: LiveData<String> = _loginSuccess

    fun login() = viewModelScope.launch(ioContext) {
        delay(1000)
        _isLoading.emit(true)
        if (email.isEmpty()) {
            _isLoading.emit(false)
            _errorMessage.emit("Enter a valid email")
            return@launch
        }
        if (password.isEmpty()) {
            _isLoading.emit(false)
            _errorMessage.emit("Enter a valid password")
            return@launch
        }

        when (val loginResource = authRepo.login(email, password)) {
            is Resource.Error -> {
                _isLoading.emit(false)
                _errorMessage.emit(loginResource.message ?: "Could not log you in.")
            }
            is Resource.Loading -> Unit
            is Resource.Success -> {
                _isLoading.emit(false)
                _loginSuccess.postValue(loginResource.data!!)
            }
        }
    }

    private val _registerSuccess = MutableLiveData<String>()
    val registerSuccess: LiveData<String> = _registerSuccess

    fun register() = viewModelScope.launch(ioContext) {
        _isLoading.emit(true)
        if (email.isEmpty()) {
            _isLoading.emit(false)
            _errorMessage.emit("Enter a valid email")
            return@launch
        }
        if (password.isEmpty()) {
            _isLoading.emit(false)
            _errorMessage.emit("Enter a valid password")
            return@launch
        }

        when (val registerResource = authRepo.register(email, password)) {
            is Resource.Error -> {
                _isLoading.emit(false)
                _errorMessage.emit(registerResource.message ?: "Could not create an account.")
            }
            is Resource.Loading -> Unit
            is Resource.Success -> {
                _isLoading.emit(false)
                _registerSuccess.postValue(registerResource.data!!)
            }

        }
    }

    private val _authenticated = MutableLiveData<Boolean>()
    val authenticated: LiveData<Boolean> = _authenticated
    fun getAuthenticationStatus() = viewModelScope.launch(ioContext) {
        _isLoading.emit(true)
        authRepo.getAuthenticationStatus().collect { authenticationStatus ->
            _isLoading.emit(false)
            if (authenticationStatus) _message.emit("Successfully logged in.")
            _authenticated.postValue(authenticationStatus)
        }
    }


}