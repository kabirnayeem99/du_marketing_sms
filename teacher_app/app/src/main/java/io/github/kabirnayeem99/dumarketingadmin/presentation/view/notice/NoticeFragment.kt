package io.github.kabirnayeem99.dumarketingadmin.presentation.view.notice


import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showConfirmationDialog
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentNoticeBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter.NoticeDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.NoticeViewModel
import kotlinx.coroutines.flow.collect

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
        lifecycleScope.launchWhenCreated {
            noticeViewModel.apply {
                getNoticeList()
                notices.observe(viewLifecycleOwner, { notices ->
                    noticeDataAdapter.differ.submitList(notices)
                })
                errorMessage.collect {
                    showErrorMessage(it)
                }
                message.collect {
                    showMessage(it)
                }
                isLoading.collect {
                    if (it) loadingIndicator.show() else loadingIndicator.dismiss()
                }
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
        showConfirmationDialog(
            "Confirm Delete",
            "Are you sure you want to delete the notice?",
            R.drawable.ic_fluent_delete_24_regular,
        ) {
            deleteNotice()

        }
    }


    private fun deleteNotice(notice: NoticeData) = noticeViewModel.deleteNoticeDataToDb(notice)


}