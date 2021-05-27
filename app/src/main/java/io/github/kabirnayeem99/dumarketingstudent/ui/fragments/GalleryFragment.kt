package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentGalleryBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.activities.MainActivity
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.GalleryDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.GalleryViewModel
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val galleryAdapter: GalleryDataAdapter by lazy {
        GalleryDataAdapter {

            val bundle = bundleOf("imageUrl" to it.imageUrl)
            binding.root.findNavController()
                .navigate(R.id.action_galleryFragment_to_imageDetailsFragment, bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        galleryViewModel.getGalleryImages().observe(viewLifecycleOwner, { resources ->

            when (resources) {
                is Resource.Error -> {
                    Log.e(TAG, "setUpRecyclerView: ${resources.message}")
                    Toast.makeText(context, "Could not get the images.", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    galleryAdapter.differ.submitList(resources.data)
                }
                else -> {

                }
            }
        })
    }


    private val galleryViewModel: GalleryViewModel by lazy {
        (activity as MainActivity).galleryViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "GalleryFragment"
    }
}