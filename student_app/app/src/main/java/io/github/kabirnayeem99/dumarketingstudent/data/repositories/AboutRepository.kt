package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.data.vo.AboutData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.AboutData.Companion.toAboutData
import io.github.kabirnayeem99.dumarketingstudent.util.Constants.ABOUT_DB_REF
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import javax.inject.Inject

class AboutRepository @Inject constructor(var db: FirebaseFirestore) {

    fun getAboutData(): LiveData<Resource<AboutData>> {

        val key: String = ABOUT_DB_REF

        val aboutLiveData = MutableLiveData<Resource<AboutData>>()

        db.collection(ABOUT_DB_REF).document(key).addSnapshotListener(
            EventListener { value, error ->

                if (error != null) {
                    aboutLiveData.value = Resource.Error(error.message ?: "Unknown error.")
                    return@EventListener
                }


                value?.let {
                    value.toAboutData().let { aboutData ->
                        aboutLiveData.value = Resource.Success(aboutData)
                    }
                }
            }
        )

        return aboutLiveData
    }
}