package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentWebViewBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.activities.MainActivity


class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wvWebsite.apply {
            loadUrl("https://www.du.ac.bd/")
            webViewClient = WebViewClient()
        }


        (activity as MainActivity).bottomNavBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as MainActivity).bottomNavBar.visibility = View.VISIBLE
    }
}