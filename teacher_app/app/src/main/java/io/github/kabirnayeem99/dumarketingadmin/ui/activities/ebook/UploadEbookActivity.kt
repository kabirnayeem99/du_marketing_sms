package io.github.kabirnayeem99.dumarketingadmin.ui.activities.ebook

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
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.ui.fragments.DelayedProgressDialog
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.CONTENT_URI
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.FILE_URI
import io.github.kabirnayeem99.dumarketingadmin.viewmodel.EbookViewModel
import java.io.File
@AndroidEntryPoint
class UploadEbookActivity : AppCompatActivity() {


    private lateinit var tiEbookName: TextInputLayout
    private lateinit var pdfFile: Uri
    private lateinit var pdfName: String
    private lateinit var tvPdfName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_ebook)
    }

    private val ebookViewModel: EbookViewModel
            by viewModels()

    fun onBtnUploadEbookClick(view: View) {

        showLoadingDialog()
        tiEbookName = findViewById(R.id.tiEbookName)


        if (!isPdfFileSelected()) {
            Toast.makeText(this, "Select a pdf file first.", Toast.LENGTH_SHORT).show()
            hideLoadingDialog()
            return
        }

        if (isPdfTitleEmpty()) {

            Toast.makeText(this, "You didn't type a title for your book.", Toast.LENGTH_SHORT)
                .show()

            tiEbookName.error = "Empty title."
            hideLoadingDialog()

            return
        }

        if (!isPdfNameSet()) {
            Toast.makeText(this, "Something is wrong with your pdf file.", Toast.LENGTH_SHORT)
                .show()

            hideLoadingDialog()

            return
        }

        uploadPdf()

    }

    private fun isPdfNameSet(): Boolean = this::pdfName.isInitialized
    private fun isPdfTitleEmpty(): Boolean = tiEbookName.editText?.text.isNullOrEmpty()
    private fun isPdfFileSelected(): Boolean = this::pdfFile.isInitialized

    private fun uploadPdf() {
        val uploadPdfTask = ebookViewModel.uploadPdf(pdfFile, pdfName)

        uploadPdfTask
            // if uploading the pdf to the storage was failed
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload your file. ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                Log.e(TAG, "uploadPdf: $e ")

                hideLoadingDialog()
            }
            // if uploading the pdf to the storage was successful
            .addOnSuccessListener { uploadTaskSnapshot ->

                val uriTask: Task<Uri> = uploadTaskSnapshot.storage.downloadUrl

                while (!uriTask.isComplete) {
                    Log.d(TAG, "uploadPdf: getting url")
                }

                val url = uriTask.result.toString()

                // after the uploading has been finished and the url has been got
                // start uploading the data to db
                uploadEbookData(url)

            }


    }

    private fun uploadEbookData(url: String) {
        val ebookData = createEbookData(url)

        val insertEbookDataToDbTask = ebookViewModel.saveEbook(ebookData)

        insertEbookDataToDbTask
            // if inserting book data to the db is failed
            .addOnFailureListener { e ->
                Toast.makeText(this, "Your ebook could not be saved.", Toast.LENGTH_SHORT)
                    .show()
                Log.e(TAG, "uploadPdf: $e")
                hideLoadingDialog()

            }
            // if inserting the data to the db was successful
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Your ebook was saved successfully.",
                    Toast.LENGTH_SHORT
                ).show()

                hideLoadingDialog()

                onBackPressed()

            }
    }

    private fun createEbookData(url: String): EbookData {
        val ebookTitle = tiEbookName.editText?.text.toString()
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
                                pdfFile,
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

                tvPdfName = findViewById(R.id.tvPdfName)

                if (pdfName.trim().isNotEmpty()) {
                    tvPdfName.text = pdfName
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

    private fun hideLoadingDialog() {
        val loadingDialog by lazy {
            DelayedProgressDialog()
        }

        loadingDialog.cancel()
    }

    companion object {
        private const val PICK_DOC_REQ_CODE = 1
        private const val TAG = "UploadEbookActivity"
    }
}