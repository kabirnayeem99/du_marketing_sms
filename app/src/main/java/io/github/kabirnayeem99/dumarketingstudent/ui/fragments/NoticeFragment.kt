package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentNoticeBinding
import io.github.kabirnayeem99.dumarketingstudent.databinding.LayoutNoticeDetailsBottomSheetBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.activities.MainActivity
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.NoticeDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.NoticeViewModel

class NoticeFragment : Fragment() {
    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!
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

        binding.rvNoticeDataList.apply {
            adapter = noticeDataAdapter
            layoutManager = LinearLayoutManager(context)
        }

        setUpNoticeData()
    }

    private fun setUpNoticeData() {
        noticeViewModel.getNoticeListLiveData().observe(viewLifecycleOwner, { noticeDataList ->
            noticeDataAdapter.differ.submitList(noticeDataList)
            Log.d(TAG, "setUpNoticeData: $noticeDataList")
        })
    }

    private val noticeViewModel: NoticeViewModel by lazy {
        (activity as MainActivity).noticeViewModel
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "NoticeFragment"
    }
}