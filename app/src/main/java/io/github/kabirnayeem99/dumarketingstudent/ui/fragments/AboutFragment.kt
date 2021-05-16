package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.AboutRepository
import io.github.kabirnayeem99.dumarketingstudent.data.vo.AboutData
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentAboutBinding
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.AboutViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.AboutViewModelFactory
import java.util.*


class AboutFragment() : Fragment() {

    private var _binding: FragmentAboutBinding? = null

    private lateinit var aboutData: AboutData

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aboutViewModel.getAboutData().observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        "Could not get the data from server.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.e(TAG, "onViewCreated: ${resource.message}")
                }

                is Resource.Success -> {
                    resource.data?.let { resourceData ->
                        aboutData = resourceData
                        loadUi(aboutData)
                    }
                }
            }
        })
    }

    private fun loadUi(aboutData: AboutData) {
        binding.tvAboutIntro.text = aboutData.intro
        setUpMapButton(aboutData.lat, aboutData.long)
        setUpMailButton(aboutData.email)
        setUpTelephoneButton(aboutData.telephone)
    }

    private fun setUpTelephoneButton(telephoneNumber: String) {
        binding.ivTelephone.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$telephoneNumber")
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "setUpTelephoneButton: $e")
                Snackbar.make(
                    binding.root,
                    "Couldn't open the dialer.",
                    Snackbar.LENGTH_LONG
                ).apply {
                    show()
                }
            }
        }
    }

    private fun setUpMailButton(emailAddress: String) {
        binding.ivMail.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                val data =
                    Uri.parse("mailto:$emailAddress?subject=From DU Marketing Student App&body=Dear Sir, \n I am an student of your department.")
                intent.data = data
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "setUpMailButton: $e")
                Snackbar.make(
                    binding.root,
                    "You don't have a mail app Installed.",
                    Snackbar.LENGTH_LONG
                ).apply {
                    show()
                }

            }
        }
    }

    private fun setUpMapButton(lat: Double, long: Double) {
        binding.ivLocation.setOnClickListener {
            try {
                val uri: String =
                    java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", lat, long)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "setUpMapButton: $e")
                Snackbar.make(
                    binding.root,
                    "You don't have Google Map installed.",
                    Snackbar.LENGTH_LONG
                ).apply {
                    show()
                }
            }
        }
    }


    private val aboutViewModel: AboutViewModel by lazy {
        val repo = AboutRepository()
        val factory = AboutViewModelFactory(repo)
        ViewModelProvider(this, factory).get(AboutViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AboutFragment"
    }
}