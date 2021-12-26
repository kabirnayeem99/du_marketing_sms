package io.github.kabirnayeem99.dumarketingstudent.data.dataSources

import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.common.util.Constants
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.data.dto.AboutDataDto.Companion.toAboutData
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.AboutData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AboutRemoteDataSource @Inject constructor(var db: FirebaseFirestore) {

    @ExperimentalCoroutinesApi
    fun getAboutData(): Flow<Resource<AboutData>> {

        val key: String = Constants.ABOUT_DB_REF

        return callbackFlow {
            db.collection(Constants.ABOUT_DB_REF).document(key)
                .addSnapshotListener { value, error ->
                    trySend(Resource.Loading())

                    if (error != null)
                        trySend(Resource.Error(error.message ?: "Unknown error."))

                    value?.let {
                        trySend(Resource.Success(value.toAboutData()))
                    }
                }
            awaitClose { cancel() }
        }
    }
}