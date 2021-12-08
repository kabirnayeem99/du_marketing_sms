package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.ebook

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.util.adapter.EbookDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.EbookViewModel

@AndroidEntryPoint
class EbookActivity : BaseActivity<ActivityEbookBinding>() {


    private val ebookViewModel: EbookViewModel by viewModels()

    override val layout: Int
        get() = R.layout.activity_ebook

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpBooksList()
        setUpObservers()
    }

    private fun setUpObservers() {
        ebookViewModel.apply {
            message.observe(this@EbookActivity, {
                showMessage(it)
            })
            errorMessage.observe(this@EbookActivity, {
                showErrorMessage(it)
            })
            isLoading.observe(this@EbookActivity, {
                binding.cpiLoading.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }

    private fun setUpBooksList() {
        ebookViewModel.getEbooks()
        binding.rvEbooks.apply {
            adapter = ebookAdapter
            layoutManager = LinearLayoutManager(this@EbookActivity)
        }

        ebookViewModel.ebookList.observe(this, { ebookList ->
            ebookAdapter.differ.submitList(ebookList)
        })
    }

    fun onFabUploadEbookClick(view: View) = openActivity(UploadEbookActivity::class.java)

    private val ebookAdapter: EbookDataAdapter by lazy {
        EbookDataAdapter {
            deleteEbook(it)
        }
    }

    private fun deleteEbook(ebook: EbookData) = ebookViewModel.deleteEbook(ebook)


}