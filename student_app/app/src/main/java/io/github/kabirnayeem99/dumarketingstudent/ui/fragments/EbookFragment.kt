package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentEbookBinding
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.EbookDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.EbookViewModel
import timber.log.Timber

@AndroidEntryPoint
class EbookFragment : Fragment() {

    private var _binding: FragmentEbookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEbookBinding.inflate(inflater, container, false)
        return binding.root
    }

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

        ebookViewModel.getEbooks().observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Error -> {
                    showSnackBar("Could not load the ebooks")
                    Log.e(TAG, "onViewCreated: ${resource.message}")
                }
                is Resource.Success -> {
                    ebookAdapter.differ.submitList(resource.data)
                }
            }

        })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AboutFragment"
    }
}