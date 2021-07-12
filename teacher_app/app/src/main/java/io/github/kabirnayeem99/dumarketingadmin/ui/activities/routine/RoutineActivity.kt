package io.github.kabirnayeem99.dumarketingadmin.ui.activities.routine

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R

@AndroidEntryPoint
class RoutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
    }

    fun onMcFirstYearRoutine(view: View) = startActivityByExtra(BatchYear.First)
    fun onMcSecondYearRoutineClick(view: View) = startActivityByExtra(BatchYear.Second)
    fun onMcThirdYearRoutine(view: View) = startActivityByExtra(BatchYear.Third)
    fun onMcFourthYearRoutineClick(view: View) = startActivityByExtra(BatchYear.Fourth)

    /**
     * Starts a new activity based on the activity class provided
     *
     * it is to simplify opening new activity for this class
     */
    private fun startActivityByExtra(batchYear: BatchYear) {
        val intent = Intent(this, RoutineDetailsActivity::class.java).apply {
            putExtra(BATCH_YEAR, batchYear.value)
        }
        startActivity(intent)
    }

    enum class BatchYear(val value: String) {
        First("1"),
        Second("2"),
        Third("3"),
        Fourth("4"),
    }

    companion object {
        const val BATCH_YEAR = "BatchYear"
    }

}