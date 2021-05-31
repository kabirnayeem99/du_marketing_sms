package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentNoticeBinding
import io.github.kabirnayeem99.dumarketingstudent.databinding.LayoutNoticeDetailsBottomSheetBinding
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.NoticeDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.NoticeViewModel
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import javax.inject.Inject


@AndroidEntryPoint
class NoticeFragment : Fragment() {
    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var scale: ScaleAnimation

    private val noticeViewModel: NoticeViewModel by viewModels()

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
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.startAnimation(scale)


        binding.rvNoticeDataList.apply {
            adapter = noticeDataAdapter
            layoutManager = LinearLayoutManager(context)

            OverScrollDecoratorHelper.setUpOverScroll(
                this,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL
            )
        }

        setUpNoticeData()
    }


    private fun setUpNoticeData() {
        noticeViewModel.getNoticeListLiveData().observe(viewLifecycleOwner, { noticeDataList ->
            noticeDataAdapter.differ.submitList(noticeDataList)
            Log.d(TAG, "setUpNoticeData: $noticeDataList")
        })
    }


    private fun showNoticeDataSheetDialog(noticeData: NoticeData) {
        val sheet = LayoutNoticeDetailsBottomSheetBinding.inflate(
            LayoutInflater.from(context)
        ).apply {

            if (noticeData.imageUrl.isNotBlank() || noticeData.imageUrl.isNotEmpty()) {
                try {
                    ivNoticeDetailedImage.visibility = View.VISIBLE
                    context?.let { context ->
                        Glide.with(context).load(noticeData.imageUrl).into(ivNoticeDetailedImage)
                    }
                } catch (e: Exception) {
                    showSnackBar("Error occured. Notification could not be shown.")
                }
            }

            tvNoticeDetailedTitle.text = noticeData.title
        }

        BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            .apply {
                setContentView(sheet.root)
                sheet.root.startAnimation(scale)
                sheet.btnCancelNoticeDetailed.setOnClickListener {
                    dismiss()
                }
                show()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "NoticeFragment"
    }
}