package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.models.SlideModel
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val imageList = ArrayList<SlideModel>() // Create image list


        imageList.add(
            SlideModel(
                "https://www.campusvarta.com/wp-content/uploads/2020/01/dhaka_university.jpg",
            )
        )
        imageList.add(
            SlideModel(
                "https://upload.wikimedia.org/wikipedia/commons/9/94/16122009_Dhaka_University_Mosque_photo1_Ranadipam_Basu.jpg",
            )
        )
        imageList.add(
            SlideModel(
                "https://upload.wikimedia.org/wikipedia/commons/2/27/Dhaka_University_03698.JPG",
            )
        )

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}