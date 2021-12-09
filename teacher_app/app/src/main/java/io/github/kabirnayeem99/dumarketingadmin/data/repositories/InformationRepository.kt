package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData.Companion.toInformationData
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.ABOUT_DB_REF
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import timber.log.Timber
import javax.inject.Inject

class InformationRepository @Inject constructor(var db: FirebaseFirestore) {

    fun upsertInformationDataToDb(informationData: InformationData): Task<Void> {

        val key: String = ABOUT_DB_REF

        return db.collection(ABOUT_DB_REF).document(key).set(informationData)
    }


    private val informationLiveData = MutableLiveData<Resource<InformationData>>()

    fun getInformationData(): LiveData<Resource<InformationData>> {

        val key: String = ABOUT_DB_REF

        db.collection(ABOUT_DB_REF).document(key).addSnapshotListener(
            EventListener { value, error ->

                if (error != null) {
                    Timber.e("getInformationData: " + error.message)
                    error.printStackTrace()
                    informationLiveData.value = Resource.Error(error.message ?: "Unknown error.")
                    return@EventListener
                }

                if (value != null) {

                    try {

                        try {
                            val informationData: InformationData = value.toInformationData()
                            informationLiveData.value = Resource.Success(informationData)
                        } catch (e: Exception) {
                            Timber.e("getInformationData: " + e.message)
                            informationLiveData.value = Resource.Error(
                                e.message ?: "Could not deserialise information data"
                            )
                        }

                    } catch (e: Exception) {
                        Timber.e("getInformationData: " + e.message)
                        e.printStackTrace()
                        informationLiveData.value =
                            Resource.Error("Empty data or could not convert the snapshot to the information data")
                    }

                }
            }
        )

        return informationLiveData
    }


    companion object {
        private const val TAG = "InformationRepository"
    }
}