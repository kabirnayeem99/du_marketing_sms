package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.ScaleAnimation
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentImageDetailsBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ImageDetailsFragment : BaseFragment<FragmentImageDetailsBinding>() {


    @Inject
    lateinit var scale: ScaleAnimation

    override val layout: Int
        get() = R.layout.fragment_image_details


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = arguments?.get("imageUrl")

        try {
            Glide.with(binding.root).load(imageUrl).into(binding.ivImageDetails)
            binding.ivImageDetails.startAnimation(scale)
        } catch (e: Exception) {
            Timber.e(e)
        }

        binding.ivImageDetails.doubleTapToZoom = true
    }

}