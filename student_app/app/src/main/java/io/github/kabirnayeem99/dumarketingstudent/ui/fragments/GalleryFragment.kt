package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentGalleryBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.activities.MainActivity
import io.github.kabirnayeem99.dumarketingstudent.ui.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.ui.adapters.GalleryDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.GalleryViewModel
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    override val layout: Int
        get() = R.layout.fragment_gallery

    private val galleryViewModel: GalleryViewModel by lazy {
        (activity as MainActivity).galleryViewModel
    }

    @Inject
    lateinit var scale: ScaleAnimation

    private val galleryAdapter: GalleryDataAdapter by lazy {
        GalleryDataAdapter {

            val bundle = bundleOf("imageUrl" to it.imageUrl)
            binding.root.findNavController()
                .navigate(R.id.action_galleryFragment_to_imageDetailsFragment, bundle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.startAnimation(scale)
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
                    Toast.makeText(context, "Could not get the images.", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    galleryAdapter.differ.submitList(resources.data)
                }
                else -> {

                }
            }
        }
    }


}