package io.github.kabirnayeem99.dumarketingadmin.ui.activities.dashboard

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
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.authintication.ui.AuthActivity
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.ebook.EbookActivity
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.faculty.FacultyActivity
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.gallery.GalleryImageActivity
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.information.InformationActivity
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.notice.NoticeActivity
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.routine.RoutineActivity
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (auth.currentUser == null) {
            navigateToAuthentication()
        } else {
            setContentView(R.layout.activity_dashboard)
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