package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.data.vo.AboutData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.AboutData.Companion.toAboutData
import io.github.kabirnayeem99.dumarketingstudent.util.Constants.ABOUT_DB_REF
import io.github.kabirnayeem99.dumarketingstudent.util.Resource

class AboutRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getAboutData(): LiveData<Resource<AboutData>> {

        val key: String = ABOUT_DB_REF

        val aboutLiveData = MutableLiveData<Resource<AboutData>>()

        db.collection(ABOUT_DB_REF).document(key).addSnapshotListener(
            EventListener { value, error ->

                if (error != null) {
                    Log.e(TAG, "getInformationData: ${error.message}")
                    error.printStackTrace()
                    aboutLiveData.value = Resource.Error(error.message ?: "Unknown error.")
                    return@EventListener
                }

                if (value != null) {

                    try {

                        try {
                            val aboutData: AboutData = value.toAboutData()
                            aboutLiveData.value = Resource.Success(aboutData)
                        } catch (e: Exception) {
                            aboutLiveData.value = Resource.Error(
                                e.message ?: "Could not de-serialise information data"
                            )
                        }

                    } catch (e: Exception) {
                        aboutLiveData.value =
                            Resource.Error("Empty data or could not convert the snapshot to the about data")
                    }

                }
            }
        )

        return aboutLiveData
    }


    companion object {
        private const val TAG = "AboutRepository"
    }
}