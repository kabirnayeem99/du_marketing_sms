package io.github.kabirnayeem99.dumarketingstudent.presentation.routine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.common.util.Preferences
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.common.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.common.util.showToast
import io.github.kabirnayeem99.dumarketingstudent.data.dto.NoticeData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentHomeBinding
import io.github.kabirnayeem99.dumarketingstudent.databinding.LayoutNoticeDetailsBottomSheetBinding
import io.github.kabirnayeem99.dumarketingstudent.presentation.MainActivity
import io.github.kabirnayeem99.dumarketingstudent.presentation.gallery.GalleryViewModel
import io.github.kabirnayeem99.dumarketingstudent.presentation.notice.NoticeDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.presentation.notice.NoticeViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_home

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    private val routineViewModel: RoutineViewModel by activityViewModels()

    private val noticeViewModel: NoticeViewModel by activityViewModels()


    @Inject
    lateinit var routineDataAdapter: RoutineDataAdapter

    @Inject
    lateinit var pref: Preferences


    private val noticeDataAdapter: NoticeDataAdapter by lazy {
        NoticeDataAdapter {
            showNoticeDataSheetDialog(it)
        }
    }

    override fun onCreated(savedInstanceState: Bundle?) {
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
                    true,
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
                        showToast("You should select a value ranging from 1 to 4")
                        input.error = "Should be between 1 to 4."
                        showAlertDialog()
                    }
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    (activity as MainActivity).onBackPressed()
                    showToast("To use this application, you must select the batch you are studying.")
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

        galleryViewModel.getRecentGallerySlideModel().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { binding.galleryImageSlider.setImageList(it) }
                }
                is Resource.Error -> {
                    binding.galleryImageSlider.visibility = View.GONE
                    Timber.e("setUpGallerySlider: ${resource.message}")
                }
            }
        }
    }

    private fun setUpRoutine() {

        pref.getBatchYear()?.let { batchYear ->
            routineViewModel.getRoutine(batchYear).observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Error -> {
                        showSnackBar("Could not load the routines for you, maybe you have not selected your batch.")
                        Timber.e("setUpRoutine: ${resource.message}")
                    }
                    else -> {
                        routineDataAdapter.differ.submitList(resource.data)
                    }
                }
            }
        }
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
                    Timber.e("lambda: ${e.message}")
                    ivNoticeDetailedImage.visibility = View.GONE
                }
            }

            tvNoticeDetailedTitle.text = noticeData.title
        }

        context?.let { ctxt -> BottomSheetDialog(ctxt) }
            ?.apply {
                setContentView(sheet.root)
                sheet.btnCancelNoticeDetailed.setOnClickListener {
                    dismiss()
                }
                show()
            }
    }

}