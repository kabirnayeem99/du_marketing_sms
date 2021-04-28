package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.NoticeRepository
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData

class NoticeViewModel(private val repo: NoticeRepository) : ViewModel() {
    fun getLatestThreeNotices(): LiveData<List<NoticeData>> = repo.getLatestThreeNotices()
    fun getNoticeListLiveData(): LiveData<List<NoticeData>> = repo.getNoticeListLiveData()
}