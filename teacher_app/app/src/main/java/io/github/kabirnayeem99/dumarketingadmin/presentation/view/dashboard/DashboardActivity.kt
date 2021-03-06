package io.github.kabirnayeem99.dumarketingadmin.presentation.view.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityDashboardBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.auth.AuthActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.DashboardViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override val layout: Int
        get() = R.layout.activity_dashboard

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpObservers()
        initViews()
    }

    private fun initViews() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_dashboard_container) as NavHostFragment
        navHostFragment.navController
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            dashboardViewModel.getAuthenticationStatus()
            dashboardViewModel.authenticated.observe(this@DashboardActivity, { isAuthenticated ->
                if (!isAuthenticated) navigateToAuthentication()
            })

            dashboardViewModel.errorMessage.collectLatest { showErrorMessage(it) }

            dashboardViewModel.message.collectLatest { message -> showMessage(message) }

            dashboardViewModel.isLoading.collectLatest { showLoading ->
                if (showLoading) loadingIndicator.show() else loadingIndicator.dismiss()
            }
        }
    }

    private fun navigateToAuthentication() {
        openActivity(AuthActivity::class.java, true)
    }


}