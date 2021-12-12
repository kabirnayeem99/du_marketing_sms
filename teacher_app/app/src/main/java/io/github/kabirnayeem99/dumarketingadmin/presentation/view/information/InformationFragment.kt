package io.github.kabirnayeem99.dumarketingadmin.presentation.view.information

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentInformationBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.InformationViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class InformationFragment : BaseFragment<FragmentInformationBinding>() {

    private val informationViewModel: InformationViewModel by activityViewModels()

    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

    override val layoutRes: Int
        get() = R.layout.fragment_information

    override fun onCreated(savedInstanceState: Bundle?) {
        subscribeObservers()
        setUpViews()
    }

    private fun setUpViews() {
        binding.btnSaveAboutInfo.animateAndOnClickListener { saveInformationData() }
    }

    private fun subscribeObservers() {
        informationViewModel.apply {
            getInformationData()
            informationData.observe(viewLifecycleOwner) { data ->
                setUpInformationEditText(data)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                errorMessage.collectLatest {
                    Timber.d("subscribeObservers -> errorMessage -> $it")
                    showErrorMessage(it)
                }
                message.collectLatest {
                    Timber.d("subscribeObservers -> message -> $it")
                    showMessage(it)
                }
                isLoading.collectLatest {
                    Timber.d("subscribeObservers -> isLoading -> $it")
                    if (it) loadingIndicator.show() else loadingIndicator.dismiss()
                }
            }
        }
    }

    private fun saveInformationData() {

        lifecycleScope.launch(ioDispatcher) {
            val email = binding.tiOfficialEmailAddress.editText?.text.toString()
            val telephone = binding.tiOfficialTelephoneNumber.editText?.text.toString()
            val intro = binding.tiOfficialIntro.editText?.text.toString()
            val lat = binding.tiLocationLatitude.editText?.text.toString().toDouble()
            val long = binding.tiLocationLongtitude.editText?.text.toString().toDouble()

            val informationData = InformationData(intro, email, telephone, lat, long)

            informationViewModel.saveInformation(informationData)
        }
    }

    private fun setUpInformationEditText(data: InformationData) {
        binding.apply {
            tiOfficialEmailAddress.editText?.setText(data.email)
            tiOfficialTelephoneNumber.editText?.setText(data.telephone)
            tiLocationLatitude.editText?.setText(data.lat.toString())
            tiLocationLongtitude.editText?.setText(data.long.toString())
            tiOfficialIntro.editText?.setText(data.intro)
        }
    }
}