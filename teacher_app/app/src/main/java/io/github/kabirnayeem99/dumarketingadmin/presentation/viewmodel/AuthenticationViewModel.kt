package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.AuthenticationRepository
import io.github.kabirnayeem99.dumarketingadmin.presentation.uiStates.AuthUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthenticationRepository,
) : ViewModel() {


    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun setEmail(email: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(email = email)
            }
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(password = password)
            }
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(name = name)
            }
        }
    }

    private var loginJob: Job? = null

    fun login() {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            _uiState.update { it.copy(isCheckingAuthentication = true) }
            try {
                when (val loginResource =
                    authRepo.login(uiState.value.email, uiState.value.password)) {
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = loginResource.message ?: "",
                                isCheckingAuthentication = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isCheckingAuthentication = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isCheckingAuthentication = false,
                                isSignedIn = true
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Could not log you in!")
                }
            }
        }
    }

    private var registerJob: Job? = null

    fun register() {
        registerJob?.cancel()
        registerJob = viewModelScope.launch {
            _uiState.update { AuthUiState().copy(isCheckingAuthentication = true) }
            try {
                when (val registerResource =
                    authRepo.register(uiState.value.email, uiState.value.password)) {
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = registerResource.message ?: "",
                                isCheckingAuthentication = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            AuthUiState(isCheckingAuthentication = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = "",
                                isCheckingAuthentication = false,
                                isSignedIn = true
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Could not log you in!")
                }
            }
        }
    }

    private var authenticationCheckJob: Job? = null

    fun getAuthenticationStatus() {
        authenticationCheckJob?.cancel()
        authenticationCheckJob = viewModelScope.launch {
            _uiState.update { it.copy(isCheckingAuthentication = true) }
            authRepo.getAuthenticationStatus().collect { authenticationStatus ->
                if (authenticationStatus) {
                    _uiState.update {
                        it.copy(isCheckingAuthentication = false, isSignedIn = true)
                    }
                } else {
                    _uiState.update {
                        it.copy(isCheckingAuthentication = false, isSignedIn = false)
                    }
                }
            }
        }
    }


}