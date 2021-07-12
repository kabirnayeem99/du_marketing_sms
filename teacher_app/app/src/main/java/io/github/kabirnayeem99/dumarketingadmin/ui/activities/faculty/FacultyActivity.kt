package io.github.kabirnayeem99.dumarketingadmin.ui.activities.faculty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.util.adapter.FacultyAdapter
import io.github.kabirnayeem99.dumarketingadmin.viewmodel.FacultyViewModel
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

        val slRvFacultyMemberList: ShimmerFrameLayout by lazy {
            findViewById(R.id.slRvFacultyMemberList)
        }

        slRvFacultyMemberList.startShimmer();

        rvFacultyMemberList.apply {
            adapter = facultyAdapter
            layoutManager = LinearLayoutManager(this@FacultyActivity)
        }

        facultyViewModel.facultyListLiveData.observe(this, { resource ->
            when (resource) {
                is Resource.Error -> {
                    Toast.makeText(
                        this,
                        "Could not get the data. ${resource.message}.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "setUpFacultyListRecyclerView: ${resource.message}")
                    slRvFacultyMemberList.stopShimmer()
                    slRvFacultyMemberList.visibility = View.GONE
                }
                is Resource.Success -> {
                    facultyAdapter.differ.submitList(resource.data)
                    slRvFacultyMemberList.stopShimmer()
                    slRvFacultyMemberList.visibility = View.GONE
                }
                else -> {
                    Log.d(TAG, "setUpFacultyListRecyclerView: loading.")
                }
            }

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

    companion object {
        private const val TAG = "FacultyActivity"
    }

}