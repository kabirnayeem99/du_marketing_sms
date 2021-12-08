package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.routine

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.vo.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.routine.RoutineActivity.Companion.BATCH_YEAR
import io.github.kabirnayeem99.dumarketingadmin.util.TimeUtilities
import io.github.kabirnayeem99.dumarketingadmin.util.TimeUtilities.getDateFromString
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.RoutineViewModel
import java.util.*

@AndroidEntryPoint
class UpsertRoutineActivity : AppCompatActivity() {

    private lateinit var routineData: RoutineData

    private lateinit var tpRoutineTime: TimePicker
    private lateinit var tiSubjectName: TextInputLayout
    private lateinit var tiFacultyName: TextInputLayout
    private lateinit var tiClassLocation: TextInputLayout

    private var isTimeChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upsert_routine)
        initViews()

        fillFieldIfUpdate()

        getRoutineTime()
    }

    private val mutableTime = MutableLiveData<String>()

    private fun getRoutineTime(): MutableLiveData<String> {


        lateinit var time: String

        time = if (!this::routineData.isInitialized) {
            TimeUtilities.getCurrentTimeInString()
        } else {
            routineData.time
        }

        mutableTime.value = time

        tpRoutineTime.setOnTimeChangedListener { _, hourOfDay, minute ->
            Log.d(TAG, "getRoutineTime: $hourOfDay:$minute")
            Date().apply {
                hours = hourOfDay
                minutes = minute
            }.also {
                time = TimeUtilities.getTimeInString(it)
                mutableTime.value = time
                isTimeChanged = true
            }
        }

        return mutableTime
    }


    // checks if this is an update or an insert
    // if it is an update, fill the field with the existing data
    private fun fillFieldIfUpdate() {
        intent.getParcelableExtra<RoutineData>(ROUTINE_PARCELABLE_EXTRA)?.let { routine ->

            // set the global routine data variable,
            routineData = routine

            // fill the field
            tiSubjectName.editText?.setText(routine.className)
            tiClassLocation.editText?.setText(routine.classLocation)
            tiFacultyName.editText?.setText(routine.facultyName)

            // set the time in the picker
            tpRoutineTime.apply {
                getDateFromString(routine.time)?.also { time ->
                    hour = time.hours
                    minute = time.minutes
                }
            }
        }


    }

    fun btnSaveRoutineClick(view: View) = saveRoutine()

    private fun saveRoutine() {
        //gets data from the text fields
        val subjectName: String = tiSubjectName.editText?.text.toString()
        val facultyName: String = tiFacultyName.editText?.text.toString()
        val classLocation: String = tiClassLocation.editText?.text.toString()

        // checks if the routine data exists, means if it is an update or not
        if (this::routineData.isInitialized) {

            routineData.className = subjectName
            routineData.facultyName = facultyName
            routineData.classLocation = classLocation

            if (isTimeChanged) {
                mutableTime.observe(this, { time ->
                    routineData.time = time
                })
            }

        } else {

            mutableTime.observe(this, { time ->
                routineData = RoutineData(time, subjectName, classLocation, facultyName)
            })

        }

        intent.getStringExtra(BATCH_YEAR)?.let { batchYear ->

            routineViewModel.insertRoutineData(routineData, batchYear)

                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save the routine", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully saved the routine.", Toast.LENGTH_SHORT)
                        .show()
                    onBackPressed()
                }
        }

    }

    private val routineViewModel: RoutineViewModel by viewModels()

    private fun initViews() {
        tiSubjectName = findViewById(R.id.tiSubjectName)
        tiFacultyName = findViewById(R.id.tiFacultyName)
        tiClassLocation = findViewById(R.id.tiClassLocation)
        tpRoutineTime = findViewById(R.id.tpRoutineTime)
    }

    companion object {
        const val ROUTINE_PARCELABLE_EXTRA = "routine_object_parcelable"
        private const val TAG = "UpsertRoutineActivity"
    }
}