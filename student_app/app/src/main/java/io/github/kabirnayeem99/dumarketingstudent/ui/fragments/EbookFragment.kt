package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentEbookBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.ui.adapters.EbookDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.EbookViewModel
import timber.log.Timber

@AndroidEntryPoint
class EbookFragment : BaseFragment<FragmentEbookBinding>() {

    override val layout: Int
        get() = R.layout.fragment_ebook

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ebookAdapter by lazy {
            EbookDataAdapter { ebookData ->
                downloadEbook(ebookData)
            }
        }

        binding.rvEbookList.apply {
            adapter = ebookAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        ebookViewModel.getEbooks().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    showSnackBar("Could not load the ebooks")
                    Timber.e("onViewCreated: ${resource.message}")
                }
                is Resource.Success -> {
                    ebookAdapter.differ.submitList(resource.data)
                }
            }

        }
    }

    private fun downloadEbook(ebookData: EbookData) {
        try {
            val url = ebookData.pdfUrl
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                setTitle(ebookData.title)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "${ebookData.title}_${ebookData.key}.pdf"
                )
            }
            val manager: DownloadManager =
                requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        } catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(context, "Could not download ${ebookData.title}", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private val ebookViewModel: EbookViewModel by viewModels()


}