package io.github.kabirnayeem99.dumarketingadmin.presentation.view.notice

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData
import io.github.kabirnayeem99.dumarketingadmin.common.util.AssetUtilities
import io.github.kabirnayeem99.dumarketingadmin.common.util.TimeUtilities
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentAddNoticeBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.NoticeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddNoticeFragment : BaseFragment<FragmentAddNoticeBinding>() {

    private var imageUrlString: String = ""
    private lateinit var bitmap: Bitmap

    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

    private val noticeViewModel: NoticeViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_add_notice

    override fun onCreated(savedInstanceState: Bundle?) {
        handleViews()
    }

    private fun handleViews() {
        binding.btnAddNotice.animateAndOnClickListener { addNotice() }
        binding.ivIconAddGalleryImage.animateAndOnClickListener { openGallery() }
    }


    private fun addNotice() {

        when {
            binding.tiNoticeTitle.editText?.text.toString().isEmpty() -> {
                binding.tiNoticeTitle.error = "Empty title"
            }
            binding.tiNoticeTitle.editText?.text.toString().length < 3 -> {
                binding.tiNoticeTitle.error = "Too short"
            }
            else -> {
                uploadNoticeData()
            }
        }
    }

    private fun createNoticeDate(): NoticeData {


        val title = binding.tiNoticeTitle.editText?.text.toString()
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
        val imageName: String =
            if (binding.tiNoticeTitle.editText?.text.toString().trim().isEmpty()) {
                "No_title_$imageFile"
            } else {
                binding.tiNoticeTitle.editText?.text.toString() + imageFile.toString()
            }


        if (imageFile != null) {

            lifecycleScope.launch(ioDispatcher) {
                val imageUrl = noticeViewModel.uploadNoticeImage(imageName, imageFile)
                if (imageUrl != null) {
                    noticeData.imageUrl = imageUrl
                    noticeViewModel.saveNotice(noticeData)
                }
            }

        } else noticeViewModel.saveNotice(noticeData)

    }


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
                            val source = ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                dataUri
                            )
                            ImageDecoder.decodeBitmap(source)

                        }
                        else -> {

                            // if it is older than Android Pie, than use deprecated getBitmap method
                            MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                dataUri
                            )

                        }
                    }.apply {

                        binding.ivNoticeImagePreview.setImageBitmap(this)
                    }
                } catch (e: Exception) {
                    showErrorMessage(e.localizedMessage ?: "")
                }

            }
        }
    }

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1
    }

}