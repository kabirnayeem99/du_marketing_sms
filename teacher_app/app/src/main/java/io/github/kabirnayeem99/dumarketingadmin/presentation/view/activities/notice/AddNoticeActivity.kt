package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.notice

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.vo.NoticeData
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.fragments.DelayedProgressDialog
import io.github.kabirnayeem99.dumarketingadmin.util.AssetUtilities
import io.github.kabirnayeem99.dumarketingadmin.util.TimeUtilities
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.NoticeViewModel

@AndroidEntryPoint
class AddNoticeActivity : AppCompatActivity() {

    private lateinit var tiNoticeTitle: TextInputLayout
    private lateinit var ivImagePreview: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var delayedProgressDialog: DelayedProgressDialog
    private var imageUrlString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notice)

    }

    private val noticeViewModel: NoticeViewModel by viewModels()

    fun onBtnAddNoticeClick(view: View) = addNotice()

    private fun addNotice() {
        tiNoticeTitle = findViewById(R.id.tiNoticeTitle)

        when {
            tiNoticeTitle.editText?.text.toString().isEmpty() -> {
                tiNoticeTitle.error = "Empty title"
            }
            tiNoticeTitle.editText?.text.toString().length < 3 -> {
                tiNoticeTitle.error = "Too short"
            }
            else -> {
                uploadNoticeData()
            }
        }
    }

    private fun createNoticeDate(): NoticeData {

        delayedProgressDialog =
            DelayedProgressDialog()

        delayedProgressDialog.show(supportFragmentManager, TAG)

        val title = tiNoticeTitle.editText?.text.toString()
        val imageUrl: String = imageUrlString


        val date = TimeUtilities.getCurrentDateInString()
        val time = TimeUtilities.getCurrentTimeInString()

        return NoticeData(title, imageUrl, date, time)
    }

    private fun uploadNoticeData() {

        val noticeData = createNoticeDate()

        var imageFile: ByteArray? = null

        if (this::bitmap.isInitialized) {
            imageFile = AssetUtilities.bitmapToJpeg(bitmap)
        }

        /*
         if there is a notice title, sets the image name to this notice title,
         else, no title
         added with the unique file name byte array
         */
        val imageName: String = if (tiNoticeTitle.editText?.text.toString().trim().isEmpty()) {
            "No_title_$imageFile"
        } else {
            tiNoticeTitle.editText?.text.toString() + imageFile.toString()
        }


        if (imageFile != null) {

            // if there is an image added
            val imageUploadTask = noticeViewModel.uploadNoticeImage(imageName, imageFile)


            imageUploadTask

                // if image could not be uploaded to storage show toast
                .addOnFailureListener { e ->
                    Log.e(TAG, "uploadNoticeData: failed to upload image $e")
                    Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show()

                    delayedProgressDialog.cancel()
                }

                // if image was uploaded proceed to insert the notice data to db
                .addOnSuccessListener {

                    // gets the storage image path

                    val uriTask: Task<Uri> = it.storage.downloadUrl

                    while (!uriTask.isComplete) {
                        Log.d(TAG, "uploadNoticeData: getting the uri")
                    }

                    noticeData.imageUrl = uriTask.result.toString()


                    noticeViewModel.insertNoticeDataToDb(noticeData)

                        // if the inserting of notice data to db was successful, go back
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "The notice was saved with the image.",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            delayedProgressDialog.cancel()

                            onBackPressed()
                        }

                        // if the data insertion was not successful show toast
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "The notice could not be saved with the image.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                }
        } else {
            // if there is no image added, just insert the data
            val insertNoticeDataTask = noticeViewModel.insertNoticeDataToDb(noticeData)
            insertNoticeDataTask.addOnFailureListener {
                Toast.makeText(
                    this,
                    "The notice could not be saved with the image.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }.addOnSuccessListener {
                Toast.makeText(this, "This notice was saved.", Toast.LENGTH_SHORT).show()
                delayedProgressDialog.cancel()
                onBackPressed()
            }
        }

    }

    fun onIvAddNoticeImageClick(view: View) = openGallery()

    private fun openGallery() {
        val pickImageIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            // gets the data uri after checking if the data is null or not
            data?.data?.let { dataUri ->

                try {
                    bitmap = when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {


                            // if this is something new like, Android Pie, use this new method
                            val source = ImageDecoder.createSource(contentResolver, dataUri)
                            ImageDecoder.decodeBitmap(source)

                        }
                        else -> {

                            // if it is older than Android Pie, than use deprecated getBitmap method
                            MediaStore.Images.Media.getBitmap(contentResolver, dataUri)

                        }
                    }.apply {

                        // sets the image to the preview
                        ivImagePreview = findViewById(R.id.ivNoticeImagePreview)
                        ivImagePreview.setImageBitmap(this)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1
        private const val TAG = "AddNoticeActivity"
    }

}