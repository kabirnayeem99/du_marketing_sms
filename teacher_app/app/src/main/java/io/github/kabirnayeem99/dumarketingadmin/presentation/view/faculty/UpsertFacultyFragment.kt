package io.github.kabirnayeem99.dumarketingadmin.presentation.view.faculty

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.util.AssetUtilities
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants.TEACHER_POSTS
import io.github.kabirnayeem99.dumarketingadmin.common.util.RegexValidatorUtils
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentUpsertFacultyBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.FacultyViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UpsertFacultyFragment : BaseFragment<FragmentUpsertFacultyBinding>() {

    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

    private val facultyViewModel: FacultyViewModel
            by activityViewModels()

    private lateinit var bitmap: Bitmap
    private var teacherPost: String = ""
    private var facultyData: FacultyData? = null

    override val layoutRes: Int
        get() = R.layout.fragment_upsert_faculty

    override fun onCreated(savedInstanceState: Bundle?) {

        checkAndImplementUpdateFunctionality()
        setUpSpinner()
    }

    private fun checkAndImplementUpdateFunctionality() {
        facultyData = arguments?.getParcelable("faculty")

        if (facultyData == null) return

        fillTextFieldIfUpdate()
    }


    private fun createFacultyData(
        name: String,
        phone: String,
        email: String,
        post: String,
    ): FacultyData {

        return FacultyData(
            name = name,
            phone = phone,
            email = email,
            post = post,
        )

    }

    private fun fillTextFieldIfUpdate() {

        facultyData?.let {

            try {
                Glide.with(this).load(it.profileImageUrl).into(binding.ivAvatar)
            } catch (e: Exception) {
                showErrorMessage("Could not load the profile picture")
            }

            binding.tiTeacherName.editText?.setText(it.name)
            binding.tiTeacherEmail.editText?.setText(it.email)
            binding.tiTeacherPhone.editText?.setText(it.phone)


            teacherPost = it.post

        }


    }

    private fun setUpSpinner() {
        val teacherPostsCategories = TEACHER_POSTS


        with(binding.sFacultyPostCat) {

            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                teacherPostsCategories
            )

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    teacherPost =
                        if (binding.sFacultyPostCat.selectedItem.toString() == teacherPostsCategories[0]
                            && teacherPost != ""
                        ) {
                            ""
                        } else {
                            binding.sFacultyPostCat.selectedItem.toString()
                        }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                    showErrorMessage("You didn't select a post for this faculty.")

                    teacherPost = "N/A"
                }

            }

        }
    }

    fun onIvAvatarClick(view: View) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQ_CODE)
    }


    fun btnSaveFacultyClick(view: View) {

        val name = binding.tiTeacherName.editText?.text.toString()
        val email = binding.tiTeacherEmail.editText?.text.toString()
        val phone = binding.tiTeacherPhone.editText?.text.toString()

        if (name.isEmpty() || name.length < 3) {
            binding.tiTeacherName.error = "Invalid name"
        } else if (phone.isEmpty() || phone.length < 11) {
            binding.tiTeacherPhone.error = "Invalid phone number"
        } else if (email.isEmpty() || !RegexValidatorUtils.validateEmail(email)) {
            binding.tiTeacherEmail.error = "Invalid email."
            binding.tiTeacherEmail.hint = "example@example.org"
        } else if (teacherPost.isEmpty()) {
            showErrorMessage("Select a position")
        } else if (!this::bitmap.isInitialized) {
            val createdFacultyData = createFacultyData(name, phone, email, teacherPost)

            facultyData?.let {
                createdFacultyData.key = it.key
                createdFacultyData.profileImageUrl = it.profileImageUrl
            }

            upsertFacultyData(createdFacultyData)

        } else {
            val createdFacultyData = createFacultyData(name, phone, email, teacherPost)

            facultyData?.let {
                createdFacultyData.key = it.key
            }

            uploadImageAndSaveData(
                createdFacultyData,
                bitmap,
            )
        }
    }

    private fun upsertFacultyData(
        facultyData: FacultyData
    ) = facultyViewModel.insertFacultyDataToDb(facultyData, facultyData.post)


    /**
     * Uploads the image and saves the data.
     */
    private fun uploadImageAndSaveData(
        facultyData: FacultyData,
        bitmap: Bitmap,
    ) {
        lifecycleScope.launch(ioDispatcher) {
            val imageName = facultyData.name.lowercase(Locale.ROOT).trim()

            val imageFile: ByteArray by lazy {
                AssetUtilities.bitmapToJpeg(bitmap)
            }

            val imageUrl = facultyViewModel.uploadImage(imageFile, imageName)

            facultyData.profileImageUrl = imageUrl

            upsertFacultyData(facultyData)
        }
    }


    /**
     * Has to do with image picker and image selection.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQ_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                try {

                    // sets the bitmap to the selected image
                    bitmap = when {

                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {

                            val source =
                                ImageDecoder.createSource(requireContext().contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)

                        }
                        else -> {

                            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)

                        }
                    }

                    try {
                        Glide.with(this@UpsertFacultyFragment)
                            .load(AssetUtilities.bitmapToJpeg(bitmap)).into(binding.ivAvatar)
                    } catch (e: Exception) {
                        Timber.e("onActivityResult: $e")
                    }

                } catch (e: Exception) {
                    Timber.e("onActivityResult: $e")
                    showErrorMessage("Could not get the image.\n${e.localizedMessage}")
                }
            }
        }
    }

    companion object {
        private const val PICK_IMAGE_REQ_CODE = 1
    }

}