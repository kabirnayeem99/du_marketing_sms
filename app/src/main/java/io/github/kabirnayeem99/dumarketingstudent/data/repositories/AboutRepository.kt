package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.github.kabirnayeem99.dumarketingstudent.util.Constants
import io.github.kabirnayeem99.dumarketingstudent.util.Constants.ABOUT_DB_PATH

class AboutRepository {
    private val db = FirebaseFirestore.getInstance()

    private val introLiveData = MutableLiveData<String>()
    fun getAboutIntro(): LiveData<String> {
        db.collection(Constants.ABOUT_DB_PATH).document("about").addSnapshotListener(
            EventListener { value, e ->
                if (e != null) {
                    Log.e(TAG, "getAboutIntro: ${e.message}")
                    e.printStackTrace()
                    return@EventListener
                }

                if (value != null) {
                    val intro = value["intro"].toString()
                    introLiveData.value = intro
                }

            }
        )
        return introLiveData
    }

    private val mailAddressLiveData = MutableLiveData<String>()
    fun getMailAddress(): LiveData<String> {
        db.collection(Constants.ABOUT_DB_PATH).document("about").addSnapshotListener(
            EventListener { value, e ->
                if (e != null) {
                    Log.e(TAG, "getMailAddress: ${e.message}")
                    e.printStackTrace()
                    return@EventListener
                }

                if (value != null) {

                    val mailAddress = value["mail_address"].toString()
                    Log.d(TAG, "getMailAddress: $value")
                    Log.d(TAG, "getMailAddress: $mailAddress")
                    mailAddressLiveData.value = mailAddress
                }


            }
        )

        return mailAddressLiveData
    }

    private val telephoneNumberLiveData = MutableLiveData<String>()
    fun getTelephoneNumber(): LiveData<String> {
        db.collection(ABOUT_DB_PATH).addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getTelephoneNumber: $error")
                }

                if (value != null) {
                    val f: QuerySnapshot = value
                    for (d in f) {
                        Log.d(TAG, "getTelephoneNumber: ${d["about"]}")
                    }
                    Log.d(TAG, "getTelephoneNumber: $value")
                }
            }
        )

        db.collection(ABOUT_DB_PATH).document("about").addSnapshotListener(
            EventListener { value, e ->
                if (e != null) {
                    Log.e(TAG, "getTelephoneNumber: ${e.message}")
                    e.printStackTrace()
                    return@EventListener
                }

                if (value != null) {
                    val number = value["telephone_number"].toString()
                    Log.d(TAG, "getTelephoneNumber: $number")
                    telephoneNumberLiveData.value = number
                }


            }
        )

        return telephoneNumberLiveData
    }


    private val locationLiveData = MutableLiveData<HashMap<String, Double>>()

    fun getLocation(): MutableLiveData<HashMap<String, Double>> {
        db.collection(Constants.ABOUT_DB_PATH).document("about").addSnapshotListener(
            EventListener { value, e ->
                if (e != null) {
                    Log.e(TAG, "getLocation: ${e.message}")
                    e.printStackTrace()
                    return@EventListener
                }

                if (value != null) {

                    var lat = 0.0
                    var long = 0.0
                    try {
                        lat = value[Constants.LATITUDE].toString().toDouble()
                        long = value[Constants.LONGTITUDE].toString().toDouble()
                    } catch (e: Exception) {
                        Log.e(TAG, "getLocation: ${e.message}")
                        e.printStackTrace()
                    }

                    Log.d(TAG, "getTelephoneNumber: $lat x $long")

                    val locationHashMap = HashMap<String, Double>().also {
                        it[Constants.LATITUDE] = lat
                        it[Constants.LONGTITUDE] = long
                    }

                    locationLiveData.value = locationHashMap
                }


            }
        )

        return locationLiveData
    }


    companion object {
        private const val TAG = "AboutRepository"
    }
}