package io.github.kabirnayeem99.dumarketingstudent.presentation.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.GalleryRepository
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(var repo: GalleryRepository) : ViewModel() {

    fun getRecentGallerySlideModel(): LiveData<Resource<List<SlideModel>>> =
        repo.getRecentGallerySlideModel()

    fun getGalleryImages() = repo.getGalleryImages()

    private val _selectedImageUrl: MutableLiveData<String> = MutableLiveData()
     val selectedImageUrl: LiveData<String> = _selectedImageUrl

    fun setSelectedImageUrl(url: String) {
        _selectedImageUrl.value = url
    }
}