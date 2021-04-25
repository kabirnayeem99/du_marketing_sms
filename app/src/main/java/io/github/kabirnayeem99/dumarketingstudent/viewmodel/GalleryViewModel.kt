package io.github.kabirnayeem99.dumarketingstudent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.GalleryRepository

class GalleryViewModel(private val repo: GalleryRepository) : ViewModel() {

    fun getRecentGallerySlideModel(): LiveData<List<SlideModel>> = repo.getRecentGallerySlideModel()
}