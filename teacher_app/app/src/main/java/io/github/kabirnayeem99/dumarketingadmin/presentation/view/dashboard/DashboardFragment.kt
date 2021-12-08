package io.github.kabirnayeem99.dumarketingadmin.presentation.view.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentDashboardBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.ebook.EbookActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.faculty.FacultyActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.gallery.GalleryImageActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.information.InformationActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.notice.NoticeActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.routine.RoutineActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.auth.AuthActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.DashboardViewModel

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val dashboardViewModel: DashboardViewModel by activityViewModels()


    override val layoutRes: Int
        get() = R.layout.fragment_dashboard

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        binding.mcNotice.setOnClickListener { onMcNoticeClick(it) }
        binding.mcGalleryImage.setOnClickListener { onMcGalleryClick(it) }
        binding.mcEbook.setOnClickListener { onMcEbookClick(it) }
        binding.mcFaculty.setOnClickListener { onMcFacultyClick(it) }
        binding.mcInfo.setOnClickListener { onMcInfoClick(it) }
        binding.mcRoutine.setOnClickListener { onMcRoutineClick(it) }
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

    private fun onMcEbookClick(view: View) = activity?.openActivity(EbookActivity::class.java)

    private fun onMcFacultyClick(view: View) = activity?.openActivity(FacultyActivity::class.java)

    private fun onMcInfoClick(view: View) = activity?.openActivity(InformationActivity::class.java)

    private fun onMcRoutineClick(view: View) = activity?.openActivity(RoutineActivity::class.java)
}