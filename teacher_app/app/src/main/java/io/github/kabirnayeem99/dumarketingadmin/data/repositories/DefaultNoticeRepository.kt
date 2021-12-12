package io.github.kabirnayeem99.dumarketingadmin.data.repositories


import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.NoticeDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.NoticeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DefaultNoticeRepository @Inject constructor(val dataSource: NoticeDataSource) :
    NoticeRepository {

    override suspend fun saveNotice(noticeData: NoticeData) = dataSource.saveNotice(noticeData)

    override suspend fun uploadNoticeImage(imageName: String, imageFile: ByteArray) =
        dataSource.uploadNoticeImage(imageName, imageFile)

    override suspend fun deleteNoticeData(noticeData: NoticeData) =
        dataSource.deleteNoticeData(noticeData)

    override fun getNoticeList() = dataSource.getNoticeList()

}