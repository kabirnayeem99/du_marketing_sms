package io.github.kabirnayeem99.dumarketingadmin.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.NoticeRepository
import io.github.kabirnayeem99.dumarketingadmin.data.vo.NoticeData
import javax.inject.Inject


@HiltViewModel
class NoticeViewModel @Inject constructor(val repo: NoticeRepository) : ViewModel() {

    fun insertNoticeDataToDb(noticeData: NoticeData) = repo.insertNoticeDataToDb(noticeData)

    fun deleteNoticeDataToDb(noticeData: NoticeData) = repo.deleteNoticeData(noticeData)

    fun uploadNoticeImage(imageName: String, imageFile: ByteArray) =
        repo.uploadNoticeImage(imageName, imageFile)

    val noticeLiveData = repo.getNoticeLiveData()
}