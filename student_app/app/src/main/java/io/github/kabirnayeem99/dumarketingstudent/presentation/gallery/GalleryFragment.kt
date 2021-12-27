package io.github.kabirnayeem99.dumarketingstudent.presentation.gallery

import android.os.Bundle
import android.view.animation.ScaleAnimation
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.common.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentGalleryBinding
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_gallery

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    @Inject
    lateinit var scale: ScaleAnimation

    private val galleryAdapter: GalleryDataAdapter by lazy {
        GalleryDataAdapter {
            galleryViewModel.setSelectedImageUrl(it.imageUrl)
            findNavController()
                .navigate(R.id.action_galleryFragment_to_imageDetailsFragment)
        }
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        binding.rvGallery.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(context, 2)

            // iOS like spring effect
            OverScrollDecoratorHelper.setUpOverScroll(
                this,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL
            )
        }

        galleryViewModel.getGalleryImages().observe(viewLifecycleOwner) { resources ->

            when (resources) {
                is Resource.Error -> {
                    Timber.e(resources.message)
                    showSnackBar("Could not get the images.").show()
                }
                is Resource.Success -> {
                    galleryAdapter.differ.submitList(resources.data)
                }
                else -> {
                    Timber.d("Loading the galleries.")
                }
            }
        }
    }


}