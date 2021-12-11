package io.github.kabirnayeem99.dumarketingadmin.presentation.view.notice


import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awesomedialog.*
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentNoticeBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter.NoticeDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.NoticeViewModel

@AndroidEntryPoint
class NoticeFragment : BaseFragment<FragmentNoticeBinding>() {
    private val noticeViewModel: NoticeViewModel by viewModels()

    private lateinit var noticeDataAdapter: NoticeDataAdapter

    override val layoutRes: Int
        get() = R.layout.fragment_notice

    override fun onCreated(savedInstanceState: Bundle?) {
        subscribeObservers()
        setUpViews()
    }

    private fun subscribeObservers() {
        noticeViewModel.apply {
            getNoticeList()
            notices.observe(viewLifecycleOwner, { notices ->
                noticeDataAdapter.differ.submitList(notices)
            })
            errorMessage.observe(viewLifecycleOwner) {
                showErrorMessage(it)
            }
            message.observe(viewLifecycleOwner) {
                showMessage(it)
            }
            isLoading.observe(viewLifecycleOwner) {
                if (it) loadingIndicator.show() else loadingIndicator.dismiss()
            }
        }
    }

    private fun setUpViews() {


        noticeDataAdapter = NoticeDataAdapter { noticeData ->
            showDeleteDialog { deleteNotice(noticeData) }
        }

        binding.rvNoticeList.apply {
            adapter = noticeDataAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.fabAddNotice.animateAndOnClickListener {
            findNavController().navigate(R.id.action_noticeFragment_to_addNoticeFragment)
        }
    }


    private fun showDeleteDialog(deleteNotice: () -> Unit) {
        AwesomeDialog.build(requireActivity())
            .title("Confirm Delete")
            .body("Are you sure you want to delete the notice?")
            .background(R.drawable.bg_rounded_dialog)
            .icon(
                R.drawable.ic_fluent_delete_24_regular,
                animateIcon = true
            )
            .onPositive(
                getString(R.string.yes),
                buttonBackgroundColor = R.drawable.bg_rounded_rectangle_button_solid,
                textColor = context?.getColor(R.color.white)
            ) {
                deleteNotice()
            }.onNegative(
                getString(R.string.no),
                buttonBackgroundColor = R.drawable.bg_rounded_rectangle_button_solid,
                textColor = context?.getColor(R.color.white)
            )
            .show()
    }


    private fun deleteNotice(notice: NoticeData) = noticeViewModel.deleteNoticeDataToDb(notice)


}