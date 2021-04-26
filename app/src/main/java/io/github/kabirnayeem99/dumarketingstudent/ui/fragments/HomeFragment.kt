package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.GalleryRepository
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.NoticeRepository
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.RoutineRepository
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentHomeBinding
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpGallerySlider()
        setUpRoutine()
        setUpLatestNotice()
    }

    private fun setUpLatestNotice() {

        noticeViewModel.getLatestNotice().observe(viewLifecycleOwner, {
            binding.tvNoticeTitle.text = it.title

            try {
                Glide.with(this)
                    .load(it.imageUrl)
                    .into(binding.ivRecentNotice)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "setUpLatestNotice: ${e.message}")
            }

        })
    }

    private fun setUpGallerySlider() {
        var imageList: List<SlideModel>

        galleryViewModel.getRecentGallerySlideModel().observe(viewLifecycleOwner, {
            imageList = it
            binding.galleryImageSlider.setImageList(imageList)
        })
    }

    private fun setUpRoutine() {

        routineViewModel.getRoutine().observe(viewLifecycleOwner, {
            try {
                Glide.with(this)
                    .load(it.imageUrl)
                    .into(binding.ivRoutine)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "setUpRoutine: ${e.message}")
            }
        })

    }


    private val galleryViewModel: GalleryViewModel by lazy {
        val repository = GalleryRepository()
        val factory = GalleryViewModelFactory(repository)
        ViewModelProvider(this, factory).get(GalleryViewModel::class.java)
    }

    private val routineViewModel: RoutineViewModel by lazy {
        val repository = RoutineRepository()
        val factory = RoutineViewModelFactory(repository)
        ViewModelProvider(this, factory).get(RoutineViewModel::class.java)
    }

    private val noticeViewModel: NoticeViewModel by lazy {
        val repository = NoticeRepository()
        val factory = NoticeViewModelFactory(repository)
        ViewModelProvider(this, factory).get(NoticeViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}