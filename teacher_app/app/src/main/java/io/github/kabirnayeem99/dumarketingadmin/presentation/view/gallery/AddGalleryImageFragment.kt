package io.github.kabirnayeem99.dumarketingadmin.presentation.view.gallery

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentAddGalleryImageBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.GalleryViewModel
import io.github.kabirnayeem99.dumarketingadmin.util.AssetUtilities
import io.github.kabirnayeem99.dumarketingadmin.util.Constants
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
            Timber.e("onIvAddGalleryImageClick: ${e.message}")
            e.printStackTrace()
            Toast.makeText(context, "Can't open your file manager.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AddGalleryImageActivity.PICK_IMAGE_REQ_CODE && resultCode == AppCompatActivity.RESULT_OK) {

            data?.let { dataUri ->
                val nullableBitmap = AssetUtilities.dataUriToBitmap(dataUri.data, contentResolver)

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
        private const val TAG = "GalleryImageActivity"
    }


}