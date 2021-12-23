package io.github.kabirnayeem99.dumarketingstudent.presentation.fragments

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.data.dto.EbookData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentEbookBinding
import io.github.kabirnayeem99.dumarketingstudent.presentation.adapters.EbookDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.presentation.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.common.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.presentation.viewmodel.EbookViewModel
import timber.log.Timber

@AndroidEntryPoint
class EbookFragment : BaseFragment<FragmentEbookBinding>() {

    override val layout: Int
        get() = R.layout.fragment_ebook

    private val ebookViewModel: EbookViewModel by activityViewModels()

    private val ebookAdapter by lazy {
        EbookDataAdapter { ebookData ->
            downloadEbook(ebookData)
        }
    }

    private var downloadId: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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
            downloadId = manager.enqueue(request)

            activity?.registerReceiver(
                onDownloadComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
        } catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(context, "Could not download ${ebookData.title}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroy() {
        activity?.unregisterReceiver(onDownloadComplete)
        super.onDestroy()
    }

    private var onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var id: Long = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            downloadId?.let { downloadId ->
                if (downloadId == id) {
                    Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT)
                        .show()
                    ebookAdapter.changeLoadingState(false)
                }
            }

        }
    }


}