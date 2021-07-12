package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentHomeBinding
import io.github.kabirnayeem99.dumarketingstudent.databinding.LayoutNoticeDetailsBottomSheetBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.activities.MainActivity
import io.github.kabirnayeem99.dumarketingstudent.util.Preferences
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.NoticeDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.RoutineDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.GalleryViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.NoticeViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.RoutineViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var scale: ScaleAnimation

    @Inject
    lateinit var routineDataAdapter: RoutineDataAdapter

    @Inject
    lateinit var pref: Preferences


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

        showAlertDialog()

        setUpGallerySlider()
        setUpRoutine()
        setUpLatestNotice()
        setUpRoutineRecyclerView()
        setUpNoticeRecyclerView()
    }


    private fun showAlertDialog() {
        if (pref.getBatchYear().isNullOrBlank() || pref.getBatchYear() == "0") {

            val viewInflated: View = LayoutInflater.from(context)
                .inflate(
                    R.layout.dialog_batch_year,
                    view?.findViewById(android.R.id.content),
                    false
                )

            val input = viewInflated.findViewById(R.id.input) as TextInputLayout


            val dialogBuilder = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("In which year you are?")
                setView(viewInflated)
                setCancelable(false)
                setPositiveButton(
                    "Save"
                ) { dialog, _ ->
                    val text = input.editText?.text.toString()
                    if (text.toInt() in 1..4) {
                        pref.setBatchYear(text)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Should be between 1 to 4.", Toast.LENGTH_SHORT)
                            .show()
                        input.error = "Should be between 1 to 4."
                    }
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    (activity as MainActivity).onBackPressed()
                    Toast.makeText(context, "You must select your year.", Toast.LENGTH_SHORT).show()
                }
            }

            dialogBuilder.show()
        }
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

        galleryViewModel.getRecentGallerySlideModel().observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { binding.galleryImageSlider.setImageList(it) }
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Could not get the images.", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "setUpGallerySlider: ${resource.message}")
                }
            }
        })
    }

    private fun setUpRoutine() {

        pref.getBatchYear()?.let { batchYear ->
            routineViewModel.getRoutine(batchYear).observe(viewLifecycleOwner, { resource ->
                when (resource) {
                    is Resource.Error -> {
                        Toast.makeText(context, "Could not get the data.", Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, "setUpRoutine: ${resource.message}")
                    }
                    else -> {
                        routineDataAdapter.differ.submitList(resource.data)
                    }
                }
            })
        }
    }

    private val galleryViewModel: GalleryViewModel by lazy {
        (activity as MainActivity).galleryViewModel
    }

    private val routineViewModel: RoutineViewModel by lazy {
        (activity as MainActivity).routineViewModel
    }

    private val noticeViewModel: NoticeViewModel by lazy {
        (activity as MainActivity).noticeViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNoticeDataSheetDialog(noticeData: NoticeData) {
        val sheet = LayoutNoticeDetailsBottomSheetBinding.inflate(
            LayoutInflater.from(context)
        ).apply {

            if (noticeData.imageUrl.isNotBlank() && noticeData.imageUrl.isNotEmpty()) {
                ivNoticeDetailedImage.visibility = View.VISIBLE
                try {
                    context?.let { context ->
                        Glide.with(context).load(noticeData.imageUrl).into(ivNoticeDetailedImage)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "lambda: ${e.message}")
                    ivNoticeDetailedImage.visibility = View.GONE
                }
            }

            tvNoticeDetailedTitle.text = noticeData.title
        }

        context?.let { ctxt -> BottomSheetDialog(ctxt, R.style.BottomSheetDialogTheme) }
            ?.apply {
                setContentView(sheet.root)
                sheet.root.startAnimation(scale)
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