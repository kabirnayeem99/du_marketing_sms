package io.github.kabirnayeem99.dumarketingadmin.presentation.view.auth

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
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityAuthBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.dashboard.DashboardActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    private val authViewModel: AuthenticationViewModel by viewModels()

    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

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
        lifecycleScope.launchWhenCreated {
            authViewModel.getAuthenticationStatus()
            authViewModel.authenticated.observe(this@AuthActivity, { isAuthenticated ->
                if (isAuthenticated) navigateToDashboard()
            })

            authViewModel.errorMessage.collectLatest { errorMessage ->
                showErrorMessage(errorMessage)
            }

            authViewModel.message.collectLatest { message ->
                showMessage(message)
            }

            authViewModel.isLoading.collectLatest { showLoading ->
                if (showLoading) showLoading() else loadingIndicator.dismiss()
            }
        }
    }

    private fun showLoading() {
        loadingIndicator.show()
    }

    private fun navigateToDashboard() {
        openActivity(DashboardActivity::class.java, true)
    }


}