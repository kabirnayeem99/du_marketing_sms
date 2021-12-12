package io.github.kabirnayeem99.dumarketingadmin.presentation.view.ebook

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.load
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants.CONTENT_URI
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants.FILE_URI
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentUploadEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter.RecommendedBookAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.EbookViewModel
import java.io.File
import java.util.*


const val PICK_DOC_REQ_CODE = 1

@AndroidEntryPoint
class UploadEbookFragment : BaseFragment<FragmentUploadEbookBinding>() {

    private val ebookViewModel: EbookViewModel by viewModels()

    private lateinit var selectedBook: EbookData
    private var pdfFile: Uri? = null
    lateinit var pdfName: String

    private val recommendedBookAdapter: RecommendedBookAdapter by lazy {
        RecommendedBookAdapter { selectRecommendedBookForUpload(it) }
    }


    override val layoutRes: Int
        get() = R.layout.fragment_upload_ebook

    override fun onCreated(savedInstanceState: Bundle?) {
        handleViews()
        subscribeObservers()
    }

    private fun handleViews() {
        binding.ivIconEbookImage.animateAndOnClickListener { onIvSelectEbookClick() }
        binding.btnUploadBook.animateAndOnClickListener { onBtnUploadEbookClick() }
        binding.ivBackButton.animateAndOnClickListener { findNavController().navigateUp() }
        binding.tvBackButton.animateAndOnClickListener { findNavController().navigateUp() }
        binding.tiEbookName.editText?.addTextChangedListener {
            if (!this::selectedBook.isInitialized) {
                val searchQuery = it.toString()
                ebookViewModel.searchBookDetails(searchQuery)
            }
        }
        binding.rvRecommendedBooks.apply {
            adapter = recommendedBookAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun subscribeObservers() {
        ebookViewModel.apply {
            ebookUrl.observe(viewLifecycleOwner) { uploadEbookData(it) }
            recommendedBookList.observe(viewLifecycleOwner) {
                recommendedBookAdapter.differ.submitList(
                    it
                )
            }
            message.observe(viewLifecycleOwner) {
                binding.tiEbookName.editText?.setText("")
                showMessage(it)
            }
            errorMessage.observe(viewLifecycleOwner) {
                binding.tiEbookName.editText?.setText("")
                showErrorMessage(it)
            }
            isLoading.observe(viewLifecycleOwner) {
                if (it) loadingIndicator.show()
                else loadingIndicator.dismiss().also {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun uploadPdf() {
        if (this::selectedBook.isInitialized) return
        if (pdfFile == null) showErrorMessage("Select a pdf first")
        else ebookViewModel.uploadPdf(pdfFile!!, pdfName)
    }

    private fun onBtnUploadEbookClick() {
        if (this::selectedBook.isInitialized) uploadEbookData(selectedBook.downloadUrl)
        returnTransition

        if (binding.tiEbookName.editText?.text.toString().trim().isEmpty()) {
            binding.tiEbookName.error = "Add a name for the pdf"
        }
        uploadPdf()

    }

    private fun uploadEbookData(url: String) {
        val ebookData = createEbookData(url)
        ebookViewModel.saveEbook(ebookData)
    }

    private fun onIvSelectEbookClick() {

        when {
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.P -> {

                val intent = Intent().apply {
                    //for opening PDF file
                    type = "application/pdf"
                    addCategory(Intent.CATEGORY_OPENABLE)
                    action = Intent.ACTION_GET_CONTENT
                    //adding Read URI permission
                    flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }

                startActivityForResult(
                    Intent.createChooser(intent, "Select a pdf file."),
                    PICK_DOC_REQ_CODE
                )
            }
            else -> {
                val intent = Intent().apply {
                    type = "application/pdf"
                    addCategory(Intent.CATEGORY_OPENABLE)
                    action = Intent.ACTION_GET_CONTENT
                    flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                startActivityForResult(
                    Intent.createChooser(intent, "Select a pdf file."),
                    PICK_DOC_REQ_CODE
                )

            }
        }
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_DOC_REQ_CODE && resultCode == AppCompatActivity.RESULT_CANCELED) {
            Toast.makeText(context, "You didn't select a pdf file.", Toast.LENGTH_SHORT).show()
            return
        }

        if (requestCode == PICK_DOC_REQ_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.let { uri ->

                pdfFile = uri

                with(pdfFile.toString()) {

                    if (startsWith(CONTENT_URI)) {
                        pdfName = "Names" + Date().time.toString()
                    } else if (startsWith(FILE_URI)) {
                        try {
                            pdfName = File(this).name
                            binding.tiEbookName.editText?.setText(pdfName)
                        } catch (e: Exception) {
                            showErrorMessage("Could not read the file name. ${e.localizedMessage}.")
                        }
                    }
                }
            }

        }
    }

    private fun selectRecommendedBookForUpload(book: EbookData) {
        selectedBook = book
        binding.tiEbookName.editText?.setText(book.name)
        binding.tvPdfName.text = book.name
        binding.ivIconEbookImage.load(book.thumbnailUrl)
    }

    private fun createEbookData(url: String): EbookData {
        val ebookTitle = binding.tiEbookName.editText?.text.toString()
        if (this::selectedBook.isInitialized) return selectedBook
        return EbookData(UUID.randomUUID().toString(), ebookTitle, url, "")
    }
}

