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
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.AboutRepository
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentAboutBinding
import io.github.kabirnayeem99.dumarketingstudent.util.Constants
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.AboutViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.AboutViewModelFactory
import java.util.*


class AboutFragment() : Fragment() {

    private var _binding: FragmentAboutBinding? = null

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

        setUpMailContact()
        setUpMap()
        setUpTelephoneContact()

        setUpAboutIntro()
    }

    private fun setUpAboutIntro() {
        aboutViewModel.getAboutIntro().observe(viewLifecycleOwner, { intro ->
            binding.tvAboutIntro.text = intro
        })
    }

    private fun setUpMailContact() {

        var mailAddresses: String = ""
        aboutViewModel.getMailAddress().observe(viewLifecycleOwner, { mailAddress ->
            mailAddresses = mailAddress
        })
        binding.ivMail.setOnClickListener {
            try {
                if (mailAddresses.isEmpty()) {
                    return@setOnClickListener
                }

                val intent = Intent(Intent.ACTION_VIEW)
                val data =
                    Uri.parse("mailto:$mailAddresses?subject=From DU Marketing Student App&body=Dear Sir, \n I am an student of your department.")
                intent.data = data
                startActivity(intent)

            } catch (e: Exception) {

                Log.e(TAG, "onViewCreated: ${e.message}")
                e.printStackTrace()

                Toast.makeText(context, "You don't have a mail app installed.", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun setUpTelephoneContact() {
        var telephoneNumber: String = ""

        aboutViewModel.getTelephoneNumber().observe(viewLifecycleOwner, {
            telephoneNumber = it
        })

        binding.ivTelephone.setOnClickListener {
            try {

                if (telephoneNumber.isEmpty()) {
                    return@setOnClickListener
                }

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$telephoneNumber")
                startActivity(intent)

            } catch (e: Exception) {
                Log.e(TAG, "onViewCreated: ${e.message}")
                e.printStackTrace()
                Toast.makeText(context, "Could not open the dialer.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpMap() {

        var lat = 0.0
        var long = 0.0
        aboutViewModel.getLocation().observe(viewLifecycleOwner, { locationHashMap ->

            lat = locationHashMap[Constants.LATITUDE] ?: 0.0
            long = locationHashMap[Constants.LONGTITUDE] ?: 0.0

        })

        binding.ivLocation.setOnClickListener {
            try {

                if (lat == 0.0 || long == 0.0) {
                    return@setOnClickListener
                }
                val uri: String =
                    java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", lat, long)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)

            } catch (e: Exception) {

                Log.e(TAG, "onViewCreated: ${e.message}")

                e.printStackTrace()

                Toast.makeText(
                    context,
                    "Your mobile doesn't have a map installed.",
                    Toast.LENGTH_SHORT
                ).show()

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