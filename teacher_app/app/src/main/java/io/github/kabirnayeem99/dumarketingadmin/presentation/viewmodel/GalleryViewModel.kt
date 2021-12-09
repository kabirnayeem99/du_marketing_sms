package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.GalleryRepository
import io.github.kabirnayeem99.dumarketingadmin.data.vo.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import javax.inject.Inject


@HiltViewModel
class GalleryViewModel @Inject constructor(val repo: GalleryRepository,) : ViewModel() {

    fun saveGalleryData(
        galleryData: GalleryData
    ): Task<Void> = repo.saveGalleryData(galleryData)

    fun uploadGalleryImage(
        category: String,
        imageFile: ByteArray
    ) = repo.uploadGalleryImage(category, imageFile)

    suspend fun deleteGalleryData(galleryData: GalleryData) = repo.deleteGalleryData(galleryData)

    fun getGalleryDataList(): LiveData<Resource<List<GalleryData>>> = repo.getGalleryDataListObservable()
}