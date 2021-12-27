package io.github.kabirnayeem99.dumarketingstudent.presentation.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.ScaleAnimation
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.data.dto.NoticeData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentNoticeBinding
import io.github.kabirnayeem99.dumarketingstudent.databinding.LayoutNoticeDetailsBottomSheetBinding
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class NoticeFragment : BaseFragment<FragmentNoticeBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_notice


    @Inject
    lateinit var scale: ScaleAnimation

    private val noticeViewModel: NoticeViewModel by activityViewModels()

    private val noticeDataAdapter: NoticeDataAdapter by lazy {
        NoticeDataAdapter {
            showNoticeDataSheetDialog(it)
        }
    }


    override fun onCreated(savedInstanceState: Bundle?) {
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
        noticeViewModel.getNoticeListLiveData().observe(viewLifecycleOwner) { noticeDataList ->
            noticeDataAdapter.differ.submitList(noticeDataList)
            Timber.d(noticeDataList.toString())
        }
    }


    private fun showNoticeDataSheetDialog(noticeData: NoticeData) {
        val sheet = LayoutNoticeDetailsBottomSheetBinding.inflate(
            LayoutInflater.from(context)
        ).apply {

            if (noticeData.imageUrl.isNotBlank() || noticeData.imageUrl.isNotEmpty()) {
                try {
                    Glide.with(requireContext()).load(noticeData.imageUrl)
                        .into(ivNoticeDetailedImage)
                    ivNoticeDetailedImage.visibility = View.VISIBLE
                } catch (e: Exception) {
                    ivNoticeDetailedImage.visibility = View.GONE
                }
            }

            tvNoticeDetailedTitle.text = noticeData.title
        }

        BottomSheetDialog(requireContext())
            .apply {
                setContentView(sheet.root)
                sheet.root.startAnimation(scale)
                sheet.btnCancelNoticeDetailed.setOnClickListener {
                    dismiss()
                }
                show()
            }
    }


}