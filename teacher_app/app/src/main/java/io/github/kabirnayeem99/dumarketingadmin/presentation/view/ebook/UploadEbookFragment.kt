package io.github.kabirnayeem99.dumarketingadmin.presentation.view.ebook

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentUploadEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.EbookViewModel
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.CONTENT_URI
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.FILE_URI
import java.io.File
import java.util.*


const val PICK_DOC_REQ_CODE = 1

@AndroidEntryPoint
class UploadEbookFragment : BaseFragment<FragmentUploadEbookBinding>() {

    var pdfFile: Uri? = null
    lateinit var pdfName: String

    private val ebookViewModel: EbookViewModel by activityViewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_upload_ebook

    override fun onCreated(savedInstanceState: Bundle?) {
        handleViews()
        subscribeObservers()
    }

    private fun handleViews() {
        binding.ivIconEbookImage.setOnClickListener { onIvSelectEbookClick() }
        binding.btnUploadBook.setOnClickListener { onBtnUploadEbookClick() }
    }

    private fun subscribeObservers() {
        ebookViewModel.apply {
            ebookUrl.observe(viewLifecycleOwner, {
                uploadEbookData(it)
            })
        }
    }

    private fun uploadPdf() {
        if (pdfFile == null) {
            showErrorMessage("Select a pdf first")
        } else {
            ebookViewModel.uploadPdf(pdfFile!!, pdfName)
        }
    }

    fun onBtnUploadEbookClick() {

        if (binding.tiEbookName.editText?.text.toString().trim().isEmpty()) {
            binding.tiEbookName.error = "Add a name for the pdf"
        }
        uploadPdf()

    }

    private fun uploadEbookData(url: String) {
        val ebookData = createEbookData(url)

        ebookViewModel.saveEbook(ebookData)

    }

    fun onIvSelectEbookClick() {

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

                        } catch (e: Exception) {

                            Toast.makeText(
                                context,
                                "Could not read the file name. ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }


            }

        }
    }

    private fun createEbookData(url: String): EbookData {
        val ebookTitle = binding.tiEbookName.editText?.text.toString()
        return EbookData(ebookTitle, url)
    }
}