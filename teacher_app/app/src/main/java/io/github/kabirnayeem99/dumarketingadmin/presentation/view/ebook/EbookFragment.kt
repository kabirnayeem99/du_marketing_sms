package io.github.kabirnayeem99.dumarketingadmin.presentation.view.ebook

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showConfirmationDialog
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter.EbookDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.EbookViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EbookFragment : BaseFragment<FragmentEbookBinding>() {

    lateinit var navController: NavController
    private val ebookViewModel: EbookViewModel by viewModels()

    private val ebookAdapter: EbookDataAdapter by lazy {
        EbookDataAdapter { deleteEbook(it) }
    }

    override val layoutRes: Int
        get() = R.layout.fragment_ebook

    override fun onCreated(savedInstanceState: Bundle?) {
        handleViews()
        setUpBooksList()
        setUpObservers()
    }

    private fun handleViews() {
        navController = findNavController()
        binding.tvUploadEbook.animateAndOnClickListener {
            navController.navigate(R.id.action_ebookFragment_to_uploadEbookFragment)
        }
        binding.ivBackButton.animateAndOnClickListener {
            navController.navigateUp()
        }

    }

    private fun setUpBooksList() {
        ebookViewModel.getEbooks()
        binding.rvEbooks.apply {
            adapter = ebookAdapter
            layoutManager = LinearLayoutManager(context)
        }

        ebookViewModel.ebookList.observe(this, { ebookList ->
            ebookAdapter.differ.submitList(ebookList)
        })
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            ebookViewModel.apply {
                message.observe(viewLifecycleOwner) { this@EbookFragment.showMessage(it) }
                errorMessage.observe(viewLifecycleOwner) { this@EbookFragment.showErrorMessage(it) }
                isLoading.observe(viewLifecycleOwner) {
                    if (it) loadingIndicator.show() else loadingIndicator.dismiss()
                }
            }
        }
    }

    private fun deleteEbook(ebook: EbookData) {
        showConfirmationDialog(
            "Confirm Delete",
            "Are you sure you want to delete \"${ebook.name}\" by ${ebook.authorName}?",
            R.drawable.ic_fluent_delete_24_regular,
        ) {
            ebookViewModel.deleteEbook(ebook)
        }
    }

}