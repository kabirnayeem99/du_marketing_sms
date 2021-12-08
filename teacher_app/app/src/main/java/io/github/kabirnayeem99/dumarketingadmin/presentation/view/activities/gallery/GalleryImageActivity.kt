package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.gallery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.vo.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.util.adapter.GalleryDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.GalleryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class GalleryImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_image)

        initViews()
        setUpRecyclerView()
        addGalleryImageClickListener()
    }

    private fun setUpRecyclerView() {

        rvGallery.apply {
            adapter = galleryDataAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                }
        }

        CoroutineScope(Dispatchers.Main).launch {
            getGalleryDataList()
        }

    }

    private fun getGalleryDataList() {
        galleryViewModel.getGalleryDataList().observe(this, { resource ->
            when (resource) {
                is Resource.Error -> {
                    Log.e(TAG, "getGalleryDataList: ${resource.message}")
                    pbGalleryLoading.visibility = View.GONE
                    Toast.makeText(this, "Could not get the images.", Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    pbGalleryLoading.visibility = View.GONE
                    galleryDataAdapter.differ.submitList(resource.data)
                    Log.d(TAG, "getGalleryDataList: ${resource.data}")
                }

                else -> {
                    pbGalleryLoading.visibility = View.VISIBLE
                }
            }
        })

    }


    private fun addGalleryImageClickListener() {
        btnAddGalleryImage.setOnClickListener {
            startActivity(Intent(this, AddGalleryImageActivity::class.java))
        }
    }


    private val galleryViewModel: GalleryViewModel by viewModels()

    private val galleryDataAdapter: GalleryDataAdapter by lazy {
        GalleryDataAdapter { deleteGalleryData(it) }
    }

    private fun deleteGalleryData(galleryData: GalleryData) {
        CoroutineScope(Dispatchers.IO).launch {

            val isDeleted = galleryViewModel.deleteGalleryData(galleryData)

            withContext(Dispatchers.Main) {

                if (isDeleted) {
                    Toast.makeText(
                        this@GalleryImageActivity,
                        "a ${galleryData.category.toLowerCase()} image has been deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this@GalleryImageActivity,
                        "the ${galleryData.category.toLowerCase()} image could not be deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    // === Views ===
    private lateinit var btnAddGalleryImage: ExtendedFloatingActionButton
    private lateinit var rvGallery: RecyclerView
    private lateinit var pbGalleryLoading: ProgressBar

    private fun initViews() {
        btnAddGalleryImage = findViewById(R.id.btnAddGalleryImage)
        rvGallery = findViewById(R.id.rvGallery)
        pbGalleryLoading = findViewById(R.id.pbGalleryLoading)
    }

    companion object {
        private const val TAG = "GalleryImageActivity"
    }

}