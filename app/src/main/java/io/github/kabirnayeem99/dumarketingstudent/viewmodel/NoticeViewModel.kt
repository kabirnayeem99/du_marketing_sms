package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.NoticeRepository
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(var repo: NoticeRepository) : ViewModel() {
    fun getLatestThreeNotices(): LiveData<List<NoticeData>> = repo.getLatestThreeNotices()
    fun getNoticeListLiveData(): LiveData<List<NoticeData>> = repo.getNoticeListLiveData()
}