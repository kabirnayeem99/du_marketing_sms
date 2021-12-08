package io.github.kabirnayeem99.dumarketingadmin.ui.activities.faculty

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.lmntrx.android.library.livin.missme.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.util.AssetUtilities
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.EXTRA_FACULTY_DATA
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.TEACHER_POSTS
import io.github.kabirnayeem99.dumarketingadmin.util.RegexValidatorUtils
import io.github.kabirnayeem99.dumarketingadmin.viewmodel.FacultyViewModel
import java.util.*
@AndroidEntryPoint
class UpsertFacultyActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    private var teacherPost: String = ""
    private var facultyData: FacultyData? = null
    private lateinit var ivAvatar: ImageView
    private lateinit var tiTeacherName: TextInputLayout
    private lateinit var tiTeacherEmail: TextInputLayout
    private lateinit var tiTeacherPhone: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upsert_faculty)
        initViews()
        checkAndImplementUpdateFunctionality()

        setUpSpinner()
    }

    private fun checkAndImplementUpdateFunctionality() {
        facultyData = intent.getParcelableExtra(EXTRA_FACULTY_DATA)

        if (facultyData == null) {
            return
        }

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
                Glide.with(this).load(it.profileImageUrl).into(ivAvatar)
            } catch (e: Exception) {
                Toast.makeText(this, "Could not load the profile picture", Toast.LENGTH_SHORT)
                    .show()
            }

            tiTeacherName.editText?.setText(it.name)
            tiTeacherEmail.editText?.setText(it.email)
            tiTeacherPhone.editText?.setText(it.phone)


            teacherPost = it.post

        }


    }

    private fun setUpSpinner() {
        val teacherPostsCategories = TEACHER_POSTS

        val sFacultyPostCat = findViewById<Spinner>(R.id.sFacultyPostCat)

        with(sFacultyPostCat) {

            adapter = ArrayAdapter(
                this@UpsertFacultyActivity,
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
                        if (sFacultyPostCat.selectedItem.toString() == teacherPostsCategories[0]
                            && teacherPost != ""
                        ) {
                            ""
                        } else {
                            sFacultyPostCat.selectedItem.toString()
                        }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                    Toast.makeText(
                        this@UpsertFacultyActivity,
                        "You didn't select a post for this faculty.",
                        Toast.LENGTH_SHORT
                    ).show()

                    teacherPost = "N/A"
                }

            }

        }
    }

    fun onIvAvatarClick(view: View) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQ_CODE)
    }

    private val facultyViewModel: FacultyViewModel
            by viewModels()

    fun btnSaveFacultyClick(view: View) {

        val name = tiTeacherName.editText?.text.toString()
        val email = tiTeacherEmail.editText?.text.toString()
        val phone = tiTeacherPhone.editText?.text.toString()

        if (name.isEmpty() || name.length < 3) {
            tiTeacherName.error = "Invalid name"
        } else if (phone.isEmpty() || phone.length < 11) {
            tiTeacherPhone.error = "Invalid phone number"
        } else if (email.isEmpty() || !RegexValidatorUtils.validateEmail(email)) {
            tiTeacherEmail.error = "Invalid email."
            tiTeacherEmail.hint = "example@example.org"
        } else if (teacherPost.isEmpty()) {
            Toast.makeText(this, "Select a position.", Toast.LENGTH_SHORT).show()
        } else if (!this::bitmap.isInitialized) {

            showProgressDialog("Saving faculty data.")

            val createdFacultyData = createFacultyData(name, phone, email, teacherPost)

            facultyData?.let {
                createdFacultyData.key = it.key
                createdFacultyData.profileImageUrl = it.profileImageUrl
            }

            upsertFacultyData(createdFacultyData)

        } else {

            showProgressDialog("Uploading the image.")

            Toast.makeText(this, "Uploading the image", Toast.LENGTH_SHORT).show()

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
    ) {

        val insertFacultyDataToDbTask =
            facultyViewModel.insertFacultyDataToDb(facultyData, facultyData.post)

        insertFacultyDataToDbTask
            .addOnFailureListener { e ->
                Toast.makeText(this, "Data could not be saved. ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                progressDialog.dismiss()
            }
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Successfully saved ${facultyData.name}'s data.",
                    Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()

                onBackPressed()
            }
    }

    /**
     * Uploads the image and saves the data.
     */
    private fun uploadImageAndSaveData(
        facultyData: FacultyData,
        bitmap: Bitmap,
    ) {
        val imageName = facultyData.name.toLowerCase(Locale.ROOT).trim()

        val imageFile: ByteArray by lazy {
            AssetUtilities.bitmapToJpeg(bitmap)
        }

        val uploadImageTask = facultyViewModel.uploadImage(imageFile, imageName)

        uploadImageTask
            .addOnFailureListener { e ->

                Log.e(TAG, "uploadImageAndSaveData: $e")

                Toast.makeText(
                    this,
                    "Image could not be saved. ${e.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                progressDialog.dismiss()
            }
            .addOnSuccessListener { uploadTaskSnapshot ->

                val uriTask = uploadTaskSnapshot.storage.downloadUrl

                while (!uriTask.isComplete) {
                }

                val url = uriTask.result.toString()

                facultyData.profileImageUrl = url

                Log.d(TAG, "uploadImageAndSaveData: $url")

                upsertFacultyData(facultyData)

            }

    }

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this@UpsertFacultyActivity)
    }

    private fun showProgressDialog(message: String) {

        progressDialog.apply {
            setMessage(message)
            setCancelable(false)
        }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menuItemDelete) {


            return if (facultyData != null) {
                showProgressDialog("Deleting...")
                facultyData?.let {
                    facultyViewModel.deleteFacultyData(it, it.post)
                        ?.addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to delete this", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                            progressDialog.dismiss()
                        }
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "Successfully deleted this", Toast.LENGTH_SHORT)
                                .show()
                            progressDialog.dismiss()
                            onBackPressed()
                        }
                }
                false
            } else {
                onBackPressed()
                true
            }
        }

        return false
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

                            val source = ImageDecoder.createSource(contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)

                        }
                        else -> {

                            MediaStore.Images.Media.getBitmap(contentResolver, uri)

                        }
                    }

                    try {
                        Glide.with(this@UpsertFacultyActivity)
                            .load(AssetUtilities.bitmapToJpeg(bitmap)).into(ivAvatar)
                    } catch (e: Exception) {
                        Log.e(TAG, "onActivityResult: $e")
                    }

                } catch (e: Exception) {

                    Log.e(TAG, "onActivityResult: $e")

                    Toast.makeText(
                        this, "Could not get the image.", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.upsert_menu, menu)
        return true
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean {
        onBackPressed()
        return true
    }

    private fun initViews() {

        tiTeacherName = findViewById(R.id.tiSubjectName)
        tiTeacherEmail = findViewById(R.id.tiTeacherEmail)
        tiTeacherPhone = findViewById(R.id.tiTeacherPhone)
        ivAvatar = findViewById(R.id.ivAvatar)


    }

    override fun onBackPressed() {
        progressDialog.onBackPressed {
            super.onBackPressed()
        }
    }


    companion object {
        private const val PICK_IMAGE_REQ_CODE = 1
        private const val TAG = "UpsertFacultyActivity"
    }

}