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
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentAboutBinding
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
    }

    private fun setUpMailContact() {
        binding.ivMail.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                val data =
                    Uri.parse("mailto:?subject=" + "DU Marketing Student App" + "&body=" + "Dear Sir, \n I am an student of your department.")
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
        binding.ivTelephone.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:880-2-8611996")
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "onViewCreated: ${e.message}")
                e.printStackTrace()
                Toast.makeText(context, "Could not open the dialer.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpMap() {
        binding.ivLocation.setOnClickListener {
            try {

                val latitude = 23.735384332659446
                val longitude = 90.39196253913155

                val uri: String =
                    java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AboutFragment"
    }
}