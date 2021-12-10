package io.github.kabirnayeem99.dumarketingadmin.presentation.view.gallery

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.fragments.DelayedProgressDialog
import io.github.kabirnayeem99.dumarketingadmin.util.AssetUtilities
import io.github.kabirnayeem99.dumarketingadmin.util.AssetUtilities.dataUriToBitmap
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.IMAGE_CATS
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.GalleryViewModel
import kotlinx.coroutines.*
@AndroidEntryPoint
class AddGalleryImageActivity : AppCompatActivity() {

    var category: String = "Other"
    private lateinit var bitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gallery_image)

    }





    private fun openImagePicker() {

        try {
            val intent =
                Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }
            startActivityForResult(
                Intent.createChooser(intent, "Choose a picture"),
                PICK_IMAGE_REQ_CODE
            )
        } catch (e: Exception) {
            Log.e(TAG, "onIvAddGalleryImageClick: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Can't open your file manager.", Toast.LENGTH_SHORT).show()
        }

    }

    // gets the image from the file manager
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQ_CODE && resultCode == RESULT_OK) {

            data?.let { dataUri ->
                val nullableBitmap = dataUriToBitmap(dataUri.data, contentResolver)

                if (nullableBitmap == null) {
                    Toast.makeText(this, "Could not get the image.", Toast.LENGTH_SHORT).show()
                } else {
                    bitmap = nullableBitmap
                    ivGalleryImagePreview = findViewById(R.id.ivGalleryImagePreview)
                    ivGalleryImagePreview.setImageBitmap(bitmap)

                }
            }

        }
    }


    // converts bitmap to jpeg and uploads it
    private fun uploadImageToGallery() {

        if (!this::bitmap.isInitialized) {
            Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show()
            hideLoadingDialog()
            return
        }

        showLoadingDialog()


        var imageFile: ByteArray? = null

        if (this::bitmap.isInitialized) {
            imageFile = AssetUtilities.bitmapToJpeg(bitmap)
        }

        if (imageFile != null) {
            val uploadTask = galleryViewModel.uploadGalleryImage(
                category,
                imageFile
            )

            uploadTask.addOnSuccessListener { uploadTaskSnapshot ->

                val uriTask = uploadTaskSnapshot.storage.downloadUrl

                while (!uriTask.isComplete) {
                }

                val imageUrl = uriTask.result.toString()

                if (imageUrl.isNotEmpty()) {
                    saveGalleryData(imageUrl)
                } else {
                    Log.e(TAG, "uploadImageToGallery: imageUrl is empty")
                    Toast.makeText(this, "Could not save the image.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
            }
        }
    }



    override fun navigateUpTo(upIntent: Intent?): Boolean {
        onBackPressed()
        return true
    }







}