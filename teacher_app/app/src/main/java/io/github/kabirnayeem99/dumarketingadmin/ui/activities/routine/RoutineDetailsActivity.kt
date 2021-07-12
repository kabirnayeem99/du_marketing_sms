package io.github.kabirnayeem99.dumarketingadmin.ui.activities.routine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.routine.RoutineActivity.Companion.BATCH_YEAR
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.util.adapter.RoutineDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.viewmodel.RoutineViewModel


@AndroidEntryPoint
class RoutineDetailsActivity : AppCompatActivity() {

    lateinit var batchYear: String

    private val routineViewModel: RoutineViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_details)

        batchYear = intent.getStringExtra(BATCH_YEAR) ?: "0"

        setUpRecyclerView(batchYear)
    }

    private fun setUpRecyclerView(batchYear: String) {
        val rvRoutineList: RecyclerView by lazy { findViewById(R.id.rvRoutineList) }
        rvRoutineList.apply {
            adapter = routineDataAdapter
            layoutManager = LinearLayoutManager(context)
        }

        routineViewModel.getRoutine(batchYear).observe(this, { resources ->

            when (resources) {
                is Resource.Error -> {
                    Toast.makeText(this, "Could not get routines from server.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, "setUpRecyclerView: ${resources.message}")
                }

                is Resource.Success -> {
                    routineDataAdapter.differ.submitList(resources.data)
                }
            }
        })

    }

    fun fabAddRoutineClick(view: View) {
        val intent = Intent(this, UpsertRoutineActivity::class.java).apply {
            putExtra(BATCH_YEAR, batchYear)
        }
        startActivity(intent)
    }


    private val routineDataAdapter: RoutineDataAdapter by lazy {
        RoutineDataAdapter { routineData ->
            val intent = Intent(this, UpsertRoutineActivity::class.java).apply {
                putExtra(UpsertRoutineActivity.ROUTINE_PARCELABLE_EXTRA, routineData)
                putExtra(BATCH_YEAR, batchYear)
            }

            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "RoutineDetailsActivity"
    }

}