package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentHomeBinding
import io.github.kabirnayeem99.dumarketingstudent.databinding.LayoutNoticeDetailsBottomSheetBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.activities.MainActivity
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.NoticeDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.RoutineDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.GalleryViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.NoticeViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.RoutineViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val routineDataAdapter: RoutineDataAdapter by lazy {
        RoutineDataAdapter()
    }
    private val noticeDataAdapter: NoticeDataAdapter by lazy {
        NoticeDataAdapter {
            showNoticeDataSheetDialog(it)
        }
    }

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
        setUpRoutineRecyclerView()
        setUpNoticeRecyclerView()
    }

    private fun setUpNoticeRecyclerView() {
        binding.rvRecentNotice.apply {
            adapter = noticeDataAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpRoutineRecyclerView() {
        binding.rvRoutine.apply {
            adapter = routineDataAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpLatestNotice() {

        noticeViewModel.getLatestThreeNotices().observe(viewLifecycleOwner, { noticeDataList ->
            noticeDataAdapter.differ.submitList(noticeDataList)
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
        routineViewModel.getRoutine().observe(viewLifecycleOwner, { routines ->
            routineDataAdapter.differ.submitList(routines)
        })
    }


    private val galleryViewModel: GalleryViewModel by lazy { (activity as MainActivity).galleryViewModel }

    private val routineViewModel: RoutineViewModel by lazy { (activity as MainActivity).routineViewModel }

    private val noticeViewModel: NoticeViewModel by lazy { (activity as MainActivity).noticeViewModel }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNoticeDataSheetDialog(noticeData: NoticeData) {
        val sheet = LayoutNoticeDetailsBottomSheetBinding.inflate(
            LayoutInflater.from(context)
        ).apply {
            try {
                context?.let { context ->
                    Glide.with(context).load(noticeData.imageUrl).into(ivNoticeDetailedImage)
                }
            } catch (e: Exception) {
                Log.e(TAG, "lambda: ${e.message}")
            }

            tvNoticeDetailedTitle.text = noticeData.title
        }
        context?.let { ctxt -> BottomSheetDialog(ctxt, R.style.BottomSheetDialogTheme) }
            ?.apply {
                setContentView(sheet.root)
                sheet.btnCancelNoticeDetailed.setOnClickListener {
                    dismiss()
                }

                show()
            }
    }


    companion object {
        private const val TAG = "HomeFragment"
    }
}