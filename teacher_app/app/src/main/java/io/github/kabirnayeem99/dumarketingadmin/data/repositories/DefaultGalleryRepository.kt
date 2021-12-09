package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.GalleryDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.GalleryRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DefaultGalleryRepository @Inject constructor(var dataSource: GalleryDataSource) :
    GalleryRepository {
    override fun getGalleryImages(): Flow<Resource<List<GalleryData>>> {
        return dataSource.getGalleryImages()
    }

    override suspend fun deleteGalleryData(galleryData: GalleryData): Resource<String> {
        return dataSource.deleteGalleryData(galleryData)
    }

    override suspend fun saveGalleryData(galleryData: GalleryData): Resource<String> {
        return saveGalleryData(galleryData)
    }

    override suspend fun uploadGalleryImage(
        category: String,
        imageFile: ByteArray
    ): Resource<String> {
        return dataSource.uploadGalleryImage(category, imageFile)
    }


}