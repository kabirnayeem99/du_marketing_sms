package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.authintication.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityAuthBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.dashboard.DashboardActivity
import io.github.kabirnayeem99.dumarketingadmin.util.RegexValidatorUtils
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {


    @Inject
    lateinit var auth: FirebaseAuth


    private lateinit var emailValidationTextWatcher: ValidationTextWatcher
    private lateinit var passwordValidationTextWatcher: ValidationTextWatcher

    override val layout: Int
        get() = R.layout.activity_auth

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpEmailTextInput()
        setUpPasswordTextInput()
        setUpLoginButton()
        setUpRegisterButton()
//        if (auth.currentUser == null) {
//            setUpEmailTextInput()
//            setUpPasswordTextInput()
//            setUpLoginButton()
//            setUpRegisterButton()
//
//        } else {
//            navigateToDashboard()
//        }
    }


    private fun setUpEmailTextInput() {
        emailValidationTextWatcher =
            ValidationTextWatcher(binding.tiUserEmail)

        binding.tiUserEmail.editText?.addTextChangedListener(emailValidationTextWatcher)

    }

    private fun setUpPasswordTextInput() {
        passwordValidationTextWatcher =
            ValidationTextWatcher(binding.tiUserPassword)

        binding.tiUserPassword.editText?.addTextChangedListener(passwordValidationTextWatcher)
    }

    private fun setUpLoginButton() {
        binding.btnLogin.setOnClickListener {
            if (emailValidationTextWatcher.validateEmail() && passwordValidationTextWatcher.validatePassword()) {
                makeButtonUnclickable()
                logIn()
            }
        }
    }

    private fun setUpRegisterButton() {
        binding.btnRegister.setOnClickListener {
            if (emailValidationTextWatcher.validateEmail() && passwordValidationTextWatcher.validatePassword()) {
                makeButtonUnclickable()
                register()
            }
        }
    }

    private fun makeButtonUnclickable() {
        binding.btnLogin.isClickable = false
        binding.btnRegister.isClickable = false
    }

    private fun makeButtonClickable() {
        binding.btnLogin.isClickable = true
        binding.btnRegister.isClickable = true
    }

    private fun logIn() {

        val email: String = binding.tiUserEmail.editText?.text.toString()
        val password: String = binding.tiUserPassword.editText?.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            navigateToDashboard()
        }.addOnFailureListener { e ->
            showErrorMessage("Could not log you in!\n${e.localizedMessage}.")

        }
    }

    private fun register() {


        val email: String = binding.tiUserEmail.editText?.text.toString()
        val password: String = binding.tiUserPassword.editText?.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            navigateToDashboard()
        }.addOnFailureListener { e ->
            showErrorMessage(e.localizedMessage ?: "")
        }
    }


    private fun navigateToDashboard() {
        openActivity(DashboardActivity::class.java, true)
    }


    private class ValidationTextWatcher(val textInputLayout: TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (textInputLayout.id) {
                R.id.tiUserEmail -> validateEmail()
                R.id.tiUserPassword -> validatePassword()
            }
        }

        fun validatePassword(): Boolean {
            if (textInputLayout.editText?.text.toString().isNullOrEmpty()) {
                textInputLayout.error = "You need a Password"
                return false
            }

            if (textInputLayout.editText?.text.toString().length < 6) {
                textInputLayout.error = "Password should be larger"
                return false
            }

            textInputLayout.isErrorEnabled = false
            return true
        }

        fun validateEmail(): Boolean {
            if (textInputLayout.editText?.text.toString().isNullOrEmpty()) {
                textInputLayout.error = "Add an email"
                return false
            }

            if (RegexValidatorUtils.validateEmail(textInputLayout.editText?.text.toString())) {
                textInputLayout.isErrorEnabled = false
                return true
            } else {
                textInputLayout.error = "Email is not valid"
                return false
            }
        }

    }
}