package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.base.BaseViewModel
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.AuthenticationRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthenticationRepository,
    private val ioContext: CoroutineDispatcher
) : BaseViewModel() {

    init {
        getAuthenticationStatus()
    }

    var email: String = ""
        set(value) {
            field = value
        }


    var password: String = ""
        set(value) {
            field = value
        }

    private val _loginSuccess = MutableLiveData<String>()
    val loginSuccess: LiveData<String> = _loginSuccess

    fun login() = viewModelScope.launch(ioContext) {
        _isLoading.postValue(true)
        if (email.isEmpty()) {
            _isLoading.postValue(false)
            _errorMessage.postValue("Enter a valid email")
            return@launch
        }
        if (password.isEmpty()) {
            _isLoading.postValue(false)
            _errorMessage.postValue("Enter a valid password")
            return@launch
        }

        when (val loginResource = authRepo.login(email, password)) {
            is Resource.Error -> {
                _isLoading.postValue(false)
                _errorMessage.postValue(loginResource.message ?: "Could not log you in.")
            }
            is Resource.Loading -> Unit
            is Resource.Success -> {
                _isLoading.postValue(false)
                _loginSuccess.postValue(loginResource.data!!)
            }
        }
    }

    private val _registerSuccess = MutableLiveData<String>()
    val registerSuccess: LiveData<String> = _registerSuccess

    fun register() = viewModelScope.launch(ioContext) {
        _isLoading.postValue(true)
        if (email.isEmpty()) {
            _isLoading.postValue(false)
            _errorMessage.postValue("Enter a valid email")
            return@launch
        }
        if (password.isEmpty()) {
            _isLoading.postValue(false)
            _errorMessage.postValue("Enter a valid password")
            return@launch
        }

        when (val registerResource = authRepo.register(email, password)) {
            is Resource.Error -> {
                _isLoading.postValue(false)
                _errorMessage.postValue(registerResource.message ?: "Could not create an account.")
            }
            is Resource.Loading -> Unit
            is Resource.Success -> {
                _isLoading.postValue(false)
                _registerSuccess.postValue(registerResource.data!!)
            }

        }
    }

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


}