package io.github.kabirnayeem99.dumarketingadmin.presentation.view.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityAuthBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.uiStates.AuthUiState
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.dashboard.DashboardActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    private val authViewModel: AuthenticationViewModel by viewModels()

    override val layout: Int
        get() = R.layout.activity_auth

    override fun onCreated(savedInstanceState: Bundle?) {
        initViews()
        setUpObservers()
    }

    private fun initViews() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_auth_container) as NavHostFragment
        navHostFragment.navController
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.apply {
                    getAuthenticationStatus()
                    uiState.collect { handleUiState(it) }
                }
            }
        }
    }

    private fun handleUiState(authUiState: AuthUiState) {
        authUiState.apply {
            if (isCheckingAuthentication) loadingIndicator.show() else loadingIndicator.dismiss()
            if (errorMessage.isNotEmpty()) showErrorMessage(errorMessage)
            if (isSignedIn) navigateToDashboard()
        }
    }

    private fun navigateToDashboard() = openActivity(DashboardActivity::class.java, true)


}