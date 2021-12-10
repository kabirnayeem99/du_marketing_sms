package io.github.kabirnayeem99.dumarketingadmin.presentation.view.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentDashboardBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.faculty.FacultyActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.gallery.GalleryImageActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.information.InformationActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.notice.NoticeActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.auth.AuthActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.DashboardViewModel

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    private lateinit var navController: NavController

    override val layoutRes: Int
        get() = R.layout.fragment_dashboard

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        navController = findNavController()
        binding.ivLogOutButton.animateAndOnClickListener { logOut() }
        binding.mcNotice.animateAndOnClickListener { onMcNoticeClick(it) }
        binding.mcGalleryImage.animateAndOnClickListener { onMcGalleryClick(it) }
        binding.mcEbook.animateAndOnClickListener { onMcEbookClick() }
        binding.mcFaculty.animateAndOnClickListener { onMcFacultyClick(it) }
        binding.mcInfo.animateAndOnClickListener { onMcInfoClick(it) }
        binding.mcRoutine.animateAndOnClickListener { onMcRoutineClick(it) }
    }

    private fun logOut() {
        dashboardViewModel.logOut()
        navigateToAuthentication()
    }

    private fun setUpObservers() {
        dashboardViewModel.authenticated.observe(this, { isAuthenticated ->
            if (!isAuthenticated) navigateToAuthentication()
        })
    }

    private fun navigateToAuthentication() {
        activity?.openActivity(AuthActivity::class.java, true)
    }

    private fun onMcNoticeClick(view: View) = activity?.openActivity(NoticeActivity::class.java)

    private fun onMcGalleryClick(view: View) =
        activity?.openActivity(GalleryImageActivity::class.java)

    private fun onMcEbookClick() =
        navController.navigate(R.id.action_dashboardFragment_to_ebookFragment)


    private fun onMcFacultyClick(view: View) = activity?.openActivity(FacultyActivity::class.java)

    private fun onMcInfoClick(view: View) = activity?.openActivity(InformationActivity::class.java)

    private fun onMcRoutineClick(view: View) =
        navController.navigate(R.id.action_dashboardFragment_to_fragmentRoutine)

}