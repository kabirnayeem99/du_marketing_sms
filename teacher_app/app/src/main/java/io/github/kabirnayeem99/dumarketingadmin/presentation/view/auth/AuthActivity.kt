package io.github.kabirnayeem99.dumarketingadmin.presentation.view.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityAuthBinding
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.dashboard.DashboardActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.AuthenticationViewModel

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
        val navController = navHostFragment.navController
    }

    private fun setUpObservers() {
        authViewModel.getAuthenticationStatus()
        authViewModel.authenticated.observe(this, { isAuthenticated ->
            if (isAuthenticated) navigateToDashboard()
        })

        authViewModel.errorMessage.observe(this, { errorMessage ->
            showErrorMessage(errorMessage)
        })

        authViewModel.message.observe(this, { message ->
            showMessage(message)
        })

        authViewModel.isLoading.observe(this, { showLoading ->
            if (showLoading) loadingIndicator.show() else loadingIndicator.dismiss()
        })
    }

    private fun navigateToDashboard() {
        openActivity(DashboardActivity::class.java, true)
    }


}