package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData

class NoticeRepository {

    private val latestNoticeListLiveData = MutableLiveData<List<NoticeData>>()
    private val noticeDataListLiveData = MutableLiveData<List<NoticeData>>()

    fun getLatestThreeNotices(): LiveData<List<NoticeData>> {
        val noticeDataList = listOf(
            NoticeData(
                "Notice Inviting Tender",
                "https://jobzalert.pk/tenders/ads_pic_directory/2020/03/07/dawn/School-Education-&-Literacy-Department-Karachi-Tender-Notice.jpg",
                "12/12/2021",
                "03:21 PM"
            ),
            NoticeData(
                "Notice Inviting Tender",
                "https://jobzalert.pk/tenders/ads_pic_directory/2020/03/07/dawn/School-Education-&-Literacy-Department-Karachi-Tender-Notice.jpg",
                "12/12/2021",
                "03:21 PM"
            ),
            NoticeData(
                "Notice Inviting Tender",
                "https://jobzalert.pk/tenders/ads_pic_directory/2020/03/07/dawn/School-Education-&-Literacy-Department-Karachi-Tender-Notice.jpg",
                "12/12/2021",
                "03:21 PM"
            ),
        )

        latestNoticeListLiveData.value = noticeDataList

        return latestNoticeListLiveData
    }

    fun getNoticeListLiveData(): MutableLiveData<List<NoticeData>> {
        val noticeDataList = mutableListOf<NoticeData>()

        noticeDataList.add(
            NoticeData(
                "Abatement activities.",
                "https://www.greatervalleyglencouncil.org/wp-content/uploads/2020/02/2020-02-grant.jpg",
                "01/11/2021",
                "02:21 PM"
            )
        )

        noticeDataList.add(
            NoticeData(
                "Notice Inviting Tender",
                "https://jobzalert.pk/tenders/ads_pic_directory/2020/03/07/dawn/School-Education-&-Literacy-Department-Karachi-Tender-Notice.jpg",
                "12/12/2021",
                "03:21 PM"
            )
        )

        noticeDataList.add(
            NoticeData(
                "Notice Inviting Tender",
                "https://jobzalert.pk/tenders/ads_pic_directory/2020/03/07/dawn/School-Education-&-Literacy-Department-Karachi-Tender-Notice.jpg",
                "12/12/2021",
                "03:21 PM"
            )
        )

        noticeDataList.add(
            NoticeData(
                "Filming Tender",
                "https://www.44thward.org/wp-content/uploads/2018/03/Filming_Notice_-_3700-3728_North_Sheffield.jpg",
                "12/12/2021",
                "04:21 PM"
            )
        )

        noticeDataListLiveData.value = noticeDataList

        return noticeDataListLiveData
    }
}