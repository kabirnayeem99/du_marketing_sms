package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.RoutineData

class RoutineRepository {

    private val routineLiveData = MutableLiveData<List<RoutineData>>()

    fun getRoutine(): LiveData<List<RoutineData>> {

        val routineList = listOf(
            RoutineData(
                "07:00", "AM", "Principles of Marketing", "MBA Building 203",
                "Imrul Kayes"
            ),

            RoutineData(
                "07:00", "AM", "Principles of Marketing", "MBA Building 203",
                "Imrul Kayes"
            ),
            RoutineData(
                "07:00", "AM", "Principles of Marketing", "MBA Building 203",
                "Imrul Kayes"
            ),
            RoutineData(
                "07:00", "AM", "Principles of Marketing", "MBA Building 203",
                "Imrul Kayes"
            ),
        )
        routineLiveData.value = routineList
        return routineLiveData
    }
}