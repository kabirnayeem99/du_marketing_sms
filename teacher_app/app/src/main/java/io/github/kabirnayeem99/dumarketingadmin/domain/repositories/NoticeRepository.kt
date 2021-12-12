package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    suspend fun saveNotice(noticeData: NoticeData): Resource<String>
    suspend fun uploadNoticeImage(imageName: String, imageFile: ByteArray): Resource<String>
    suspend fun deleteNoticeData(noticeData: NoticeData): Resource<String>
    fun getNoticeList(): Flow<Resource<List<NoticeData>>>
}