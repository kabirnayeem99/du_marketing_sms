package io.github.kabirnayeem99.dumarketingadmin.presentation.view.gallery

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentAddGalleryImageBinding
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.GalleryViewModel
import io.github.kabirnayeem99.dumarketingadmin.common.util.AssetUtilities
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddGalleryImageFragment : BaseFragment<FragmentAddGalleryImageBinding>() {
    var category: String = "Other"
    private lateinit var bitmap: Bitmap

    override val layoutRes: Int
        get() = R.layout.fragment_add_gallery_image

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreated(savedInstanceState: Bundle?) {
        initViews()
        setUpSpinnerImageCat()
    }

    private fun initViews() {
        binding.btnAddImage.setOnClickListener(::onBtnAddGalleryImageClick)
        binding.ivGalleryImagePreview.setOnClickListener(::onIvAddGalleryImageClick)
    }


    private fun saveGalleryData(imageUrl: String) {
        galleryViewModel.saveGalleryData(GalleryData(category, imageUrl))
    }

    private fun onBtnAddGalleryImageClick(view: View) = uploadImageToGallery()
    private fun onIvAddGalleryImageClick(view: View) = openImagePicker()


    private fun uploadImageToGallery() = lifecycleScope.launch {

        if (!this@AddGalleryImageFragment::bitmap.isInitialized) {
            galleryViewModel.startLoading()
            showErrorMessage("Please select an image first.")
            return@launch
        }


        var imageFile: ByteArray? = null

        if (this@AddGalleryImageFragment::bitmap.isInitialized) {
            imageFile = AssetUtilities.bitmapToJpeg(bitmap)
        }

        if (imageFile != null) {
            val imageUrl = galleryViewModel.uploadGalleryImage(
                category,
                imageFile
            )

            if (!imageUrl.isNullOrEmpty()) {
                saveGalleryData(imageUrl)
            } else {
                Timber.e("uploadImageToGallery: imageUrl is empty")
                showErrorMessage("Could not save the image.")
                return@launch
            }
        }
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
            Timber.e("onIvAddGalleryImageClick: ${e.message}")
            e.printStackTrace()
            Toast.makeText(context, "Can't open your file manager.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQ_CODE && resultCode == AppCompatActivity.RESULT_OK) {

            data?.let { dataUri ->
                val contentResolver = requireActivity().contentResolver
                val nullableBitmap =
                    AssetUtilities.dataUriToBitmap(dataUri.data, contentResolver)

                if (nullableBitmap == null) {
                    showErrorMessage("Could not get the image")
                } else {
                    bitmap = nullableBitmap
                    binding.ivGalleryImagePreview.setImageBitmap(bitmap)
                }
            }

        }
    }


    private fun setUpSpinnerImageCat() {

        val imageCats: Array<String> = Constants.IMAGE_CATS
        with(binding.sImageCat) {
            adapter =
                ArrayAdapter(
                    context,
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
                        category = binding.sImageCat.selectedItem.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        category = "Other"
                    }
                }
        }
    }

    companion object {
        private const val PICK_IMAGE_REQ_CODE = 1
    }

}