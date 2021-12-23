package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.data.dto.NoticeData
import io.github.kabirnayeem99.dumarketingstudent.data.dto.NoticeData.Companion.toNoticeDataList
import io.github.kabirnayeem99.dumarketingstudent.common.util.Constants.NOTICE_DB_REF
import javax.inject.Inject

class NoticeRepository @Inject constructor(var db: FirebaseFirestore) {


    private val latestNoticeListLiveData = MutableLiveData<List<NoticeData>>()
    private val noticeDataListLiveData = MutableLiveData<List<NoticeData>>()

    fun getLatestThreeNotices(): LiveData<List<NoticeData>> {

        lateinit var noticeDataList: List<NoticeData>


        val ref = db.collection(NOTICE_DB_REF).orderBy("date").limitToLast(3)

        ref.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    return@EventListener
                }

                if (value != null) {
                    noticeDataList = value.toNoticeDataList()
                    latestNoticeListLiveData.value = noticeDataList
                }
            }
        )

        return latestNoticeListLiveData
    }

    fun getNoticeListLiveData(): MutableLiveData<List<NoticeData>> {

        lateinit var noticeDataList: List<NoticeData>

        val ref = db.collection(NOTICE_DB_REF)

        ref.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getLatestThreeNotices: ${error.message}")
                    error.printStackTrace()
                    return@EventListener
                }

                if (value != null) {
                    noticeDataList = value.toNoticeDataList()
                    noticeDataListLiveData.value = noticeDataList
                }
            }
        )

        return noticeDataListLiveData
    }

    companion object {
        private const val TAG = "NoticeRepository"
    }
}