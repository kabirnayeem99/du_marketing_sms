package io.github.kabirnayeem99.dumarketingadmin.presentation.view.information

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentInformationBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.InformationViewModel

@AndroidEntryPoint
class InformationFragment : BaseFragment<FragmentInformationBinding>() {

    private val informationViewModel: InformationViewModel by activityViewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_information

    override fun onCreated(savedInstanceState: Bundle?) {
        subscribeObservers()
        setUpViews()
    }

    private fun setUpViews() {
        binding.btnSaveAboutInfo.setOnClickListener { saveInformationData() }
    }

    private fun subscribeObservers() {
        informationViewModel.apply {
            getInformationData()
            informationData.observe(viewLifecycleOwner) { data ->
                setUpInformationEditText(data)
            }
            errorMessage.observe(viewLifecycleOwner) {
                showErrorMessage(it)
            }
            message.observe(viewLifecycleOwner) {
                showMessage(it)
            }
            isLoading.observe(viewLifecycleOwner) {
                if (it) loadingIndicator.show() else loadingIndicator.dismiss()
            }
        }
    }

    private fun saveInformationData() {

        val email = binding.tiOfficialEmailAddress.editText?.text.toString()
        val telephone = binding.tiOfficialTelephoneNumber.editText?.text.toString()
        val intro = binding.tiOfficialIntro.editText?.text.toString()
        val lat = binding.tiLocationLatitude.editText?.text.toString().toDouble()
        val long = binding.tiLocationLongtitude.editText?.text.toString().toDouble()

        val informationData = InformationData(intro, email, telephone, lat, long)

        informationViewModel.upsertInformation(informationData)
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