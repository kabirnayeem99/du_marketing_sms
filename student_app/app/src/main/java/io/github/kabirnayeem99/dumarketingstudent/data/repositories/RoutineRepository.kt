package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.data.dto.RoutineData
import io.github.kabirnayeem99.dumarketingstudent.data.dto.RoutineData.Companion.toRoutineDataList
import io.github.kabirnayeem99.dumarketingstudent.common.util.Constants
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import javax.inject.Inject

class RoutineRepository @Inject constructor(var db: FirebaseFirestore) {

    private val routineLiveData = MutableLiveData<Resource<List<RoutineData>>>()
    fun getRoutine(batchYear: String): MutableLiveData<Resource<List<RoutineData>>> {
        db.collection(Constants.ROUTINE_DB_REF)
            .document(batchYear)
            .collection("routines").addSnapshotListener(
                EventListener { value, error ->

                    if (error != null) {
                        routineLiveData.value =
                            Resource.Error(error.message ?: "Firebase fire store error.")
                        return@EventListener
                    }


                    value?.toRoutineDataList()?.let { routineDataList ->
                        routineLiveData.value = Resource.Success(routineDataList)
                    }
                })
        return routineLiveData
    }
}