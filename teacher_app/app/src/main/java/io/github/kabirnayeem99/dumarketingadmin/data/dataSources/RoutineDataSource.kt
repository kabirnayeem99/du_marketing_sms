package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingadmin.data.vo.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.data.vo.RoutineData.Companion.toRoutineDataList
import io.github.kabirnayeem99.dumarketingadmin.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoutineDataSource @Inject constructor(val db: FirebaseFirestore) {
    suspend fun insertRoutineData(routineData: RoutineData, batchYear: String): Resource<String> {

        return try {
            val key = routineData.time.replace("\\s".toRegex(), "")

            db.collection(Constants.ROUTINE_DB_REF)
                .document(batchYear)
                .collection("routines")
                .document(key)
                .set(routineData).await()
            Resource.Success(key)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not save the data. ")
        }
    }

    @ExperimentalCoroutinesApi
    fun getRoutine(batchYear: String) = callbackFlow {

        db.collection(Constants.ROUTINE_DB_REF)
            .document(batchYear)
            .collection("routines").addSnapshotListener { value, error ->

                if (error != null || value == null) trySend(
                    Resource.Error(
                        error?.message ?: "The routine could not be gotten."
                    )
                )

                if (value != null) {
                    trySend(Resource.Success(value.toRoutineDataList()))
                }
            }

        awaitClose { cancel() }
    }
}