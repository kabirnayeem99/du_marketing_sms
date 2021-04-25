package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.RoutineData

class RoutineRepository {

    private val routineLiveData = MutableLiveData<RoutineData>()

    fun getRoutine(): LiveData<RoutineData> {
        val url =
            "https://1.bp.blogspot.com/-Onykh7uN-XA/V5bQK2b7RqI/AAAAAAAABTg/nEo1VxregHMQa-mnPJA2u4nB3uZFpl8owCEw/s1600/Schedule.png"

        routineLiveData.value = RoutineData(url)
        return routineLiveData
    }
}