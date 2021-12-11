package io.github.kabirnayeem99.dumarketingadmin.presentation.view.routine

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.data.model.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentUpsertRoutineBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.RoutineViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.TimeUtilities
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class UpsertRoutineFragment : BaseFragment<FragmentUpsertRoutineBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_upsert_routine
    private val routineViewModel: RoutineViewModel by activityViewModels()


    private var isTimeChanged: Boolean = false
    private lateinit var routineData: RoutineData

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreated(savedInstanceState: Bundle?) {
        handleViews()
        fillFieldIfUpdate()
        getRoutineTime()
    }

    private fun handleViews() {
        binding.btnSaveRotuine.setOnClickListener { btnSaveRoutineClick() }
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

        binding.tpRoutineTime.setOnTimeChangedListener { _, hourOfDay, minute ->
            Timber.d("getRoutineTime: $hourOfDay:$minute")
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
    @RequiresApi(Build.VERSION_CODES.M)
    private fun fillFieldIfUpdate() {

        routineViewModel.selectedRoutine
            ?.let { routine ->

                // set the global routine data variable,
                routineData = routine

                // fill the field
                binding.tiSubjectName.editText?.setText(routine.className)
                binding.tiClassLocation.editText?.setText(routine.classLocation)
                binding.tiFacultyName.editText?.setText(routine.facultyName)

                // set the time in the picker
                binding.tpRoutineTime.apply {
                    TimeUtilities.getDateFromString(routine.time)?.also { time ->
                        hour = time.hours
                        minute = time.minutes
                    }
                }
            }
    }

    private fun btnSaveRoutineClick() = saveRoutine()

    private fun saveRoutine() {
        //gets data from the text fields
        val subjectName: String = binding.tiSubjectName.editText?.text.toString()
        val facultyName: String = binding.tiFacultyName.editText?.text.toString()
        val classLocation: String = binding.tiClassLocation.editText?.text.toString()

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

        val batchYear = routineViewModel.batchYear
        routineViewModel.insertRoutineData(routineData, batchYear)
    }
}