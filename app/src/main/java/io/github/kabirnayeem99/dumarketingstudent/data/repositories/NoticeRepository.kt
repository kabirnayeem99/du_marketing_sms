package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData

class NoticeRepository {

    private val latestNoticeLiveData = MutableLiveData<NoticeData>()

    fun getLatestNotice(): LiveData<NoticeData> {
        val noticeData = NoticeData(
            "Notice Inviting Tender",
            "https://jobzalert.pk/tenders/ads_pic_directory/2020/03/07/dawn/School-Education-&-Literacy-Department-Karachi-Tender-Notice.jpg"
        )

        latestNoticeLiveData.value = noticeData

        return latestNoticeLiveData
    }
}