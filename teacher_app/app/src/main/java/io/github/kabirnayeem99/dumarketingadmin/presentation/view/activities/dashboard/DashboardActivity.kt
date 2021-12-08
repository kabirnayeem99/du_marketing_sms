package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityDashboardBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.authintication.ui.AuthActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.ebook.EbookActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.faculty.FacultyActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.gallery.GalleryImageActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.information.InformationActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.notice.NoticeActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.routine.RoutineActivity
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    @Inject
    lateinit var auth: FirebaseAuth

    override val layout: Int
        get() = R.layout.activity_dashboard

    override fun onCreated(savedInstanceState: Bundle?) {
        if (auth.currentUser == null) {
            navigateToAuthentication()
        }
    }


    private fun navigateToAuthentication() {
        Intent(this, AuthActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            .also { intent ->
                startActivity(intent)
            }

        ActivityCompat.finishAffinity(this@DashboardActivity);
    }

    fun onMcNoticeClick(view: View) = startActivityByClass(NoticeActivity::class.java)

    fun onMcGalleryClick(view: View) = startActivityByClass(GalleryImageActivity::class.java)

    fun onMcEbookClick(view: View) = startActivityByClass(EbookActivity::class.java)

    fun onMcFacultyClick(view: View) = startActivityByClass(FacultyActivity::class.java)

    fun onMcInfoClick(view: View) = startActivityByClass(InformationActivity::class.java)

    fun onMcRoutineClick(view: View) = startActivityByClass(RoutineActivity::class.java)

    /**
     * Starts a new activity based on the activity class provided
     *
     * it is to simplify opening new activity for this class
     */
    private fun startActivityByClass(cls: Class<*>?) {
        val intent = Intent(this@DashboardActivity, cls)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out -> {
                auth.signOut()
                if (auth.currentUser == null) {
                    navigateToAuthentication()
                }
                return true
            }
        }
        return false
    }
}