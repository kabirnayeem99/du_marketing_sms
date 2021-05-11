package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentGalleryBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.activities.MainActivity
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.GalleryDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.GalleryViewModel
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

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
            layoutManager = GridLayoutManager(context, 3)

            // iOS like spring effect
            OverScrollDecoratorHelper.setUpOverScroll(
                this,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL
            )
        }

        galleryViewModel.getGalleryImages().observe(viewLifecycleOwner, { galleryList ->
            galleryAdapter.differ.submitList(galleryList)
        })
    }


    private val galleryViewModel: GalleryViewModel by lazy {
        (activity as MainActivity).galleryViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}