package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.faculty

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter.FacultyAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.FacultyViewModel

@AndroidEntryPoint
class FacultyActivity : AppCompatActivity() {
    private lateinit var rvFacultyMemberList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty)

        initViews()

        setUpFacultyListRecyclerView()
    }


    private val facultyViewModel: FacultyViewModel by viewModels()

    private fun setUpFacultyListRecyclerView() {

        facultyViewModel.getFacultyList()

        val slRvFacultyMemberList: ShimmerFrameLayout by lazy {
            findViewById(R.id.slRvFacultyMemberList)
        }

        slRvFacultyMemberList.startShimmer()

        rvFacultyMemberList.apply {
            adapter = facultyAdapter
            layoutManager = LinearLayoutManager(this@FacultyActivity)
        }

        facultyViewModel.facultyListObservable.observe(this, {
            slRvFacultyMemberList.visibility = View.GONE
            facultyAdapter.differ.submitList(it)
        })

    }


    private val facultyAdapter: FacultyAdapter by lazy {
        FacultyAdapter()
    }


    fun onFabAddFacultyClick(view: View) = startActivityByClass(UpsertFacultyActivity::class.java)

    /**
     * Starts a new activity based on the activity class provided
     *
     * it is to simplify opening new activity for this class
     */
    private fun startActivityByClass(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    /**
     * Initialises the views
     */
    private fun initViews() {
        rvFacultyMemberList = findViewById(R.id.rvFacultyMemberLists)
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        onBackPressed()
        return true
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}