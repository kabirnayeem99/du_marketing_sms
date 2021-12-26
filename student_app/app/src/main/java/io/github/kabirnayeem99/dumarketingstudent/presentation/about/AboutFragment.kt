package io.github.kabirnayeem99.dumarketingstudent.presentation.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.common.util.Constants.GMAIL_PACKAGE_NAME
import io.github.kabirnayeem99.dumarketingstudent.common.util.Constants.GOOGLE_MAPS_PACKAGE_NAME
import io.github.kabirnayeem99.dumarketingstudent.common.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentAboutBinding
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.AboutData
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    private val aboutViewModel: AboutViewModel by activityViewModels()


    override val layoutRes: Int
        get() = R.layout.fragment_about

    override fun onCreated(savedInstanceState: Bundle?) {
        subscribeQuery()
    }

    private fun subscribeQuery() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                aboutViewModel.uiState.collect {
                    handleUiState(it)
                }
            }
        }
    }

    private fun handleUiState(uiState: AboutUiState) {
        uiState.apply {
            if (isLoading) loadingIndicator.show() else loadingIndicator.dismiss()
            if (message.isNotEmpty()) showSnackBar(message)
            loadUi(about)
        }
    }


    private fun loadUi(aboutData: AboutData) {
        binding.tvAboutIntro.text = aboutData.intro
        setUpMapButton(aboutData.lat, aboutData.long)
        setUpMailButton(aboutData.email)
        setUpTelephoneButton(aboutData.telephone)
        setUpWebViewButton()
    }

    private fun setUpTelephoneButton(telephoneNumber: String) {
        binding.ivTelephone.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$telephoneNumber")
                startActivity(intent)
            } catch (e: Exception) {
                Timber.e(e)
                showSnackBar(
                    "You are using a chromebook or a tablet." +
                            "\n" +
                            "which doesn't support calling feature."
                ).apply {
                    anchorView = binding.snackbarPlacement
                    show()
                }
            }
        }
    }

    private fun setUpMailButton(emailAddress: String) {
        binding.ivMail.setOnClickListener {
            try {

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    putExtra(Intent.EXTRA_EMAIL, emailAddress)
                    putExtra(Intent.EXTRA_SUBJECT, "From " + getString(R.string.app_name))
                    data = Uri.parse("mailto:")
                }

                startActivity(intent)
            } catch (e: Exception) {
                Timber.e(e)
                showSnackBar("You don't have a mail app installed.")
                    .apply {
                        anchorView = binding.snackbarPlacement
                        setAction("Install") {
                            navigateToPlayStore(GMAIL_PACKAGE_NAME)
                        }
                        show()
                    }

            }
        }
    }


    private fun setUpWebViewButton() {
        binding.ivWeb.setOnClickListener {
            findNavController().navigate(R.id.toWebViewFragment)
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
                Timber.e(e)
                showSnackBar("You don't have Google Map installed.").apply {
                    anchorView = binding.snackbarPlacement
                    setAction("Install") {
                        navigateToPlayStore(GOOGLE_MAPS_PACKAGE_NAME)
                    }
                    show()
                }
            }
        }
    }

    private fun navigateToPlayStore(packageName: String) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (e: Exception) {
            Timber.e(e)
            showSnackBar("Could not find $packageName")
        }
    }


}