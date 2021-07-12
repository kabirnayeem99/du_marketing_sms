package io.github.kabirnayeem99.dumarketingadmin.ui.activities.gallery

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
import io.github.kabirnayeem99.dumarketingadmin.data.vo.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.ui.fragments.DelayedProgressDialog
import io.github.kabirnayeem99.dumarketingadmin.util.AssetUtilities
import io.github.kabirnayeem99.dumarketingadmin.util.AssetUtilities.dataUriToBitmap
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.IMAGE_CATS
import io.github.kabirnayeem99.dumarketingadmin.viewmodel.GalleryViewModel
import kotlinx.coroutines.*
@AndroidEntryPoint
class AddGalleryImageActivity : AppCompatActivity() {

    var category: String = "Other"
    private lateinit var bitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gallery_image)
        initViews()
        setUpSpinnerImageCat()
    }

    // sets up the drop down menu
    private fun setUpSpinnerImageCat() {

        val imageCats: Array<String> = IMAGE_CATS
        with(sImageCat) {
            adapter =
                ArrayAdapter(
                    this@AddGalleryImageActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    imageCats
                )
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        category = sImageCat.selectedItem.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        category = "Other"
                    }
                }
        }
    }


    fun onBtnAddGalleryImageClick(view: View) = uploadImageToGallery()
    fun onIvAddGalleryImageClick(view: View) = openImagePicker()


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

    private fun saveGalleryData(imageUrl: String) {
        galleryViewModel.saveGalleryData(GalleryData(category, imageUrl))

            .addOnFailureListener { e ->
                Log.e(TAG, "saveGalleryData: $e")
                Toast.makeText(this, "Could not save the image.", Toast.LENGTH_SHORT).show()
            }

            .addOnSuccessListener {
                Toast.makeText(this, "Successfully saved the image.", Toast.LENGTH_SHORT).show()
                onBackPressed()
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

    private val galleryViewModel: GalleryViewModel by viewModels()


    lateinit var sImageCat: Spinner
    private lateinit var ivIconAddGalleryImage: ImageView
    private lateinit var ivGalleryImagePreview: ImageView

    private fun initViews() {
        sImageCat = findViewById(R.id.sImageCat)
        ivIconAddGalleryImage = findViewById(R.id.ivIconAddGalleryImage)
        ivGalleryImagePreview = findViewById(R.id.ivGalleryImagePreview)
    }

    companion object {
        private const val PICK_IMAGE_REQ_CODE = 1
        private const val TAG = "GalleryImageActivity"
    }

}