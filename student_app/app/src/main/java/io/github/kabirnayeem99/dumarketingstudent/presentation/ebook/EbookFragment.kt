package io.github.kabirnayeem99.dumarketingstudent.presentation.ebook

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.common.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentEbookBinding
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.EbookData
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class EbookFragment : BaseFragment<FragmentEbookBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_ebook

    private val ebookViewModel: EbookViewModel by activityViewModels()

    private val ebookAdapter by lazy {
        EbookDataAdapter { ebookData ->
            downloadEbook(ebookData)
        }
    }

    private var downloadId: Long? = null

    override fun onCreated(savedInstanceState: Bundle?) {
        handleViews()
        subscribeToQuery()
    }

    private fun subscribeToQuery() {
        ebookViewModel.fetchEbooks()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ebookViewModel.uiState.collect { handleUiState(it) }
            }
        }
    }

    private fun handleUiState(uiState: EbookUiState) {
        uiState.apply {
            if (isLoading) loadingIndicator.show() else loadingIndicator.dismiss()
            if (message.isNotEmpty()) showSnackBar(message)
            if (ebookList.isNotEmpty()) ebookAdapter.differ.submitList(ebookList)
        }
    }

    private fun handleViews() {
        binding.rvEbookList.apply {
            adapter = ebookAdapter
            layoutManager = LinearLayoutManager(requireContext())
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
            Timber.e(e, "Failed in downloading ebook " + e.localizedMessage)
            showSnackBar("Could not download ${ebookData.title}.")
        }
    }

    override fun onDestroy() {
        try {
            activity?.unregisterReceiver(onDownloadComplete)
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }
        super.onDestroy()
    }

    private var onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id: Long = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

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