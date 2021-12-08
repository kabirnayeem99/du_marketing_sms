package io.github.kabirnayeem99.dumarketingadmin.presentation.view.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityDashboardBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.auth.AuthActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.DashboardViewModel

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
        val navController = navHostFragment.navController
    }

    private fun setUpObservers() {
        dashboardViewModel.authenticated.observe(this, { isAuthenticated ->
            if (!isAuthenticated) navigateToAuthentication()
        })
    }

    private fun navigateToAuthentication() {
        openActivity(AuthActivity::class.java, true)
    }


}