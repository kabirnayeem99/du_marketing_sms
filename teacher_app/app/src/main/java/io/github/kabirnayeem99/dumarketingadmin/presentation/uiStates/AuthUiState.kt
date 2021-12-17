package io.github.kabirnayeem99.dumarketingadmin.presentation.uiStates

data class AuthUiState(
    val isSignedIn: Boolean = false,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val errorMessage: String = "",
    val isCheckingAuthentication: Boolean = false,
)