package io.github.kabirnayeem99.dumarketingadmin.presentation.view.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentLoginBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.dashboard.DashboardActivity
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.AuthenticationViewModel
import io.github.kabirnayeem99.dumarketingadmin.util.RegexValidatorUtils

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val authViewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var navController: NavController

    private lateinit var emailValidationTextWatcher: ValidationTextWatcher
    private lateinit var passwordValidationTextWatcher: ValidationTextWatcher

    override val layoutRes: Int
        get() = R.layout.fragment_login

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        navController = findNavController()
        setUpEmailTextInput()
        setUpPasswordTextInput()
        setUpLoginButton()
        setUpRegisterButton()
    }

    private fun setUpRegisterButton() {
        binding.btnRegister.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setUpObservers() {
        authViewModel.apply {
            loginSuccess.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) navigateToDashboard()
            })
        }
    }


    private fun logIn() {
        authViewModel.email = binding.tiUserEmail.editText?.text.toString()
        authViewModel.password = binding.tiUserPassword.editText?.text.toString()

        authViewModel.login()
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
                logIn()
            }
        }
    }

    private fun navigateToDashboard() {
        activity?.openActivity(DashboardActivity::class.java, true)
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
            if (textInputLayout.editText?.text.toString().isEmpty()) {
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
            if (textInputLayout.editText?.text.toString().isEmpty()) {
                textInputLayout.error = "Add an email"
                return false
            }

            return if (RegexValidatorUtils.validateEmail(textInputLayout.editText?.text.toString())) {
                textInputLayout.isErrorEnabled = false
                true
            } else {
                textInputLayout.error = "Email is not valid"
                false
            }
        }

    }

}