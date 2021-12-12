package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getGalleryImages(): Flow<Resource<List<GalleryData>>>
    suspend fun deleteGalleryData(galleryData: GalleryData): Resource<String>
    suspend fun saveGalleryData(galleryData: GalleryData): Resource<String>
    suspend fun uploadGalleryImage(category: String, imageFile: ByteArray): Resource<String>
}