package io.github.kabirnayeem99.dumarketingadmin.data.repositories


import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingadmin.data.vo.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.data.vo.RoutineData.Companion.toRoutineDataList
import io.github.kabirnayeem99.dumarketingadmin.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import javax.inject.Inject

class RoutineRepository @Inject constructor(var db: FirebaseFirestore) {


    fun insertRoutineData(routineData: RoutineData, batchYear: String): Task<Void> {

        // remove all whitespaces
        val key = routineData.time.replace("\\s".toRegex(), "")

        return db.collection(Constants.ROUTINE_DB_REF)
            .document(batchYear)
            .collection("routines")
            .document(key)
            .set(routineData)
    }

    private val routineLiveData = MutableLiveData<Resource<List<RoutineData>>>()

    fun getRoutine(batchYear: String): MutableLiveData<Resource<List<RoutineData>>> {

        db.collection(Constants.ROUTINE_DB_REF)
            .document(batchYear)
            .collection("routines").addSnapshotListener(EventListener { value, error ->

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