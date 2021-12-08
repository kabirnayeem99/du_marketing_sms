package io.github.kabirnayeem99.dumarketingadmin.presentation.view.ebook

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.EbookViewModel
import io.github.kabirnayeem99.dumarketingadmin.util.adapter.EbookDataAdapter

@AndroidEntryPoint
class EbookFragment : BaseFragment<FragmentEbookBinding>() {
    private val ebookViewModel: EbookViewModel by activityViewModels()

    private val ebookAdapter: EbookDataAdapter by lazy {
        EbookDataAdapter {
            deleteEbook(it)
        }
    }

    override val layoutRes: Int
        get() = R.layout.fragment_ebook

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpBooksList()
        setUpObservers()
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
        ebookViewModel.apply {
            message.observe(viewLifecycleOwner, {
                showMessage(it)
            })
            errorMessage.observe(viewLifecycleOwner, {
                showErrorMessage(it)
            })
            isLoading.observe(viewLifecycleOwner, {
                binding.cpiLoading.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }

    private fun deleteEbook(ebook: EbookData) = ebookViewModel.deleteEbook(ebook)

}