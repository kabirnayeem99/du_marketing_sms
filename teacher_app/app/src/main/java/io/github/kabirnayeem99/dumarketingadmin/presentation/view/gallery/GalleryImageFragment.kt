package io.github.kabirnayeem99.dumarketingadmin.presentation.view.gallery

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentGalleryImageBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter.GalleryDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.GalleryViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GalleryImageFragment : BaseFragment<FragmentGalleryImageBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_gallery_image

    private val galleryDataAdapter: GalleryDataAdapter by lazy {
        GalleryDataAdapter { deleteGalleryData(it) }
    }

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpRecyclerView()
        addGalleryImageClickListener()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        lifecycleScope.launchWhenCreated {
            galleryViewModel.getGalleryDataList()
            galleryViewModel.galleryDataList.observe(viewLifecycleOwner) { galleryDataList ->
                galleryDataAdapter.differ.submitList(galleryDataList)
            }

            galleryViewModel.errorMessage.collect {
                showErrorMessage(it)
            }

            galleryViewModel.message.collect {
                showMessage(it)
            }

            galleryViewModel.isLoading.collect {
                if (it) loadingIndicator.show() else loadingIndicator.dismiss()
            }
        }

    }

    private fun setUpRecyclerView() {
        binding.rvGallery.apply {
            adapter = galleryDataAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                }
        }
    }


    private fun addGalleryImageClickListener() {
        binding.btnAddGalleryImage.setOnClickListener {
            findNavController().navigate(R.id.action_galleryImageFragment_to_addGalleryImageFragment)
        }
    }

    private fun deleteGalleryData(galleryData: GalleryData) =
        galleryViewModel.deleteGalleryData(galleryData)


}