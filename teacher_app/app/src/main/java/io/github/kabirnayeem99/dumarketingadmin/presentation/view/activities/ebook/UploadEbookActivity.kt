package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.ebook

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityUploadEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.fragments.DelayedProgressDialog
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.CONTENT_URI
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.FILE_URI
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.EbookViewModel
import java.io.File

@AndroidEntryPoint
class UploadEbookActivity : BaseActivity<ActivityUploadEbookBinding>() {

    var pdfFile: Uri? = null
    lateinit var pdfName: String

    private val ebookViewModel: EbookViewModel
            by viewModels()

    override val layout: Int
        get() = R.layout.activity_upload_ebook

    override fun onCreated(savedInstanceState: Bundle?) {
        subscribeObservers()
    }

    private fun subscribeObservers() {
        ebookViewModel.apply {
            ebookUrl.observe(this@UploadEbookActivity, {
                uploadEbookData(it)
            })
            message.observe(this@UploadEbookActivity, {
                showMessage(it)
            })
            errorMessage.observe(this@UploadEbookActivity, {
                showErrorMessage(it)
            })
        }
    }


    fun onBtnUploadEbookClick(view: View) {

        if (binding.tiEbookName.editText?.text.toString().trim().isEmpty()) {
            binding.tiEbookName.error = "Add a name for the pdf"
        }
        uploadPdf()

    }


    private fun uploadPdf() {
        if (pdfFile == null) {
            showErrorMessage("Select a pdf first")
        } else {
            ebookViewModel.uploadPdf(pdfFile!!, pdfName)
        }
    }

    private fun uploadEbookData(url: String) {
        val ebookData = createEbookData(url)

        ebookViewModel.saveEbook(ebookData)

    }

    private fun createEbookData(url: String): EbookData {
        val ebookTitle = binding.tiEbookName.editText?.text.toString()
        return EbookData(ebookTitle, url)
    }


    fun onIvSelectEbookClick(view: View) {

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

        if (requestCode == PICK_DOC_REQ_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "You didn't select a pdf file.", Toast.LENGTH_SHORT).show()
            return
        }

        if (requestCode == PICK_DOC_REQ_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->

                pdfFile = uri

                with(pdfFile.toString()) {

                    if (startsWith(CONTENT_URI)) {

                        var cursor: Cursor? = null

                        try {

                            cursor = this@UploadEbookActivity.contentResolver.query(
                                pdfFile!!,
                                null,
                                null,
                                null,
                                null,
                            )

                            if (cursor != null && cursor.moveToFirst()) {
                                pdfName =
                                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            }

                        } catch (e: Exception) {

                            Toast.makeText(
                                this@UploadEbookActivity,
                                "Pdf name could not be found",
                                Toast.LENGTH_SHORT
                            ).show()

                        } finally {

                            cursor?.close()

                        }
                    } else if (startsWith(FILE_URI)) {
                        try {

                            pdfName = File(this).name

                        } catch (e: Exception) {

                            Toast.makeText(
                                this@UploadEbookActivity,
                                "Could not read the file name. ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }


            }

        }
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoadingDialog() {
        val loadingDialog by lazy {
            DelayedProgressDialog()
        }

        loadingDialog.show(supportFragmentManager, TAG)
    }


    companion object {
        private const val PICK_DOC_REQ_CODE = 1
        private const val TAG = "UploadEbookActivity"
    }
}