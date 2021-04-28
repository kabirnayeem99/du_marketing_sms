package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentImageDetailsBinding

class ImageDetailsFragment : Fragment() {

    private var _binding: FragmentImageDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            Glide.with(binding.root).load(arguments?.get("imageUrl")).into(binding.ivImageDetails)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "onViewCreated: ${e.message}")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ImageDetailsFragment"
    }
}