package io.github.kabirnayeem99.dumarketingadmin.ui.activities.information

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.vo.InformationData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.viewmodel.InformationViewModel
@AndroidEntryPoint
class InformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        initViews()

        getInformationData()
        saveListener()

    }

    private val informationViewModel: InformationViewModel by viewModels()

    // Gets the data from the server, if it exists
    private fun getInformationData() {

        informationViewModel.getInformationData().observe(this, { resource ->

            when (resource) {

                is Resource.Error -> {
                    Toast.makeText(this, "Could not get the data.", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    resource.data?.let { informationData ->
                        setUpInformationEditText(informationData)
                    }
                }

            }
        })
    }


    // === Save Information Data Section
    private fun saveListener() {

        btnSaveAboutInfo.setOnClickListener {
            saveInformationData()
        }

    }

    private fun saveInformationData() {

        try {
            val email = tiOfficialEmailAddress.editText?.text.toString()
            val telephone = tiOfficialTelephoneNumber.editText?.text.toString()
            val intro = tiOfficialIntro.editText?.text.toString()

            val lat = tiLocationLatitude.editText?.text.toString().toDouble()
            val long = tiLocationLongtitude.editText?.text.toString().toDouble()

            val informationData = InformationData(intro, email, telephone, lat, long)

            informationViewModel.upsertInformation(informationData)

                .addOnFailureListener { e ->
                    e.printStackTrace().also {
                        Toast.makeText(
                            this,
                            "Could not save the data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }

                .addOnSuccessListener {
                    onBackPressed()

                }

        } catch (e: Exception) {
            // if fails to convert the lat and lang to double
            e.printStackTrace().also {
                Toast.makeText(
                    this,
                    "Latitude and longtitude should be decimal number.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }


    // === Progress Dialog Section ===


    // === Views Section ===

    private lateinit var tiOfficialEmailAddress: TextInputLayout
    private lateinit var tiOfficialTelephoneNumber: TextInputLayout
    private lateinit var tiLocationLatitude: TextInputLayout
    private lateinit var tiLocationLongtitude: TextInputLayout
    private lateinit var tiOfficialIntro: TextInputLayout
    private lateinit var btnSaveAboutInfo: Button

    private fun initViews() {
        tiOfficialEmailAddress = findViewById(R.id.tiOfficialEmailAddress)
        tiOfficialTelephoneNumber = findViewById(R.id.tiOfficialTelephoneNumber)
        tiLocationLatitude = findViewById(R.id.tiLocationLatitude)
        tiLocationLongtitude = findViewById(R.id.tiLocationLongtitude)
        tiOfficialIntro = findViewById(R.id.tiOfficialIntro)
        btnSaveAboutInfo = findViewById(R.id.btnSaveAboutInfo)
    }

    // fills the edit text field when there is already an information data
    // in the server and this is an update, rather than an insert
    private fun setUpInformationEditText(data: InformationData) {
        tiOfficialEmailAddress.editText?.setText(data.email)
        tiOfficialTelephoneNumber.editText?.setText(data.telephone)
        tiLocationLatitude.editText?.setText(data.lat.toString())
        tiLocationLongtitude.editText?.setText(data.long.toString())
        tiOfficialIntro.editText?.setText(data.intro)

    }

    companion object {
        private const val TAG = "InformationActivity"
    }
}