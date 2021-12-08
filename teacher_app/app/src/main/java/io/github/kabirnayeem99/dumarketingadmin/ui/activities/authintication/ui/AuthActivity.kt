package io.github.kabirnayeem99.dumarketingadmin.ui.activities.authintication.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.ui.activities.dashboard.DashboardActivity
import io.github.kabirnayeem99.dumarketingadmin.util.RegexValidatorUtils
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {


    @Inject
    lateinit var auth: FirebaseAuth

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this)
    }

    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var tiUserEmail: TextInputLayout
    private lateinit var tiUserPassword: TextInputLayout

    private lateinit var emailValidationTextWatcher: ValidationTextWatcher
    private lateinit var passwordValidationTextWatcher: ValidationTextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (auth.currentUser == null) {
            setContentView(R.layout.activity_auth)
            initViews()

            setUpEmailTextInput()
            setUpPasswordTextInput()
            setUpLoginButton()
            setUpRegisterButton()

        } else {
            navigateToDashboard()
        }
    }

    private fun setUpEmailTextInput() {
        emailValidationTextWatcher =
            ValidationTextWatcher(tiUserEmail)

        tiUserEmail.editText?.addTextChangedListener(emailValidationTextWatcher)

    }

    private fun setUpPasswordTextInput() {
        passwordValidationTextWatcher =
            ValidationTextWatcher(tiUserPassword)

        tiUserPassword.editText?.addTextChangedListener(passwordValidationTextWatcher)
    }

    private fun setUpLoginButton() {
        btnLogin.setOnClickListener {
            if (emailValidationTextWatcher.validateEmail() && passwordValidationTextWatcher.validatePassword()) {
                makeButtonUnclickable()
                logIn()
            }
        }
    }

    private fun setUpRegisterButton() {
        btnRegister.setOnClickListener {
            if (emailValidationTextWatcher.validateEmail() && passwordValidationTextWatcher.validatePassword()) {
                makeButtonUnclickable()
                register()
            }
        }
    }

    private fun makeButtonUnclickable() {
        btnLogin.isClickable = false
        btnRegister.isClickable = false
    }

    private fun makeButtonClickable() {
        btnLogin.isClickable = true
        btnRegister.isClickable = true
    }

    private fun logIn() {
        progressDialog.setMessage("Logging in...")
        progressDialog.show()

        val email: String = tiUserEmail.editText?.text.toString()
        val password: String = tiUserPassword.editText?.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            navigateToDashboard()
            progressDialog.dismiss()
        }.addOnFailureListener { e ->
            e.printStackTrace()

            Snackbar.make(btnLogin, "Could not log you in. \n${e.message}", Snackbar.LENGTH_LONG)
                .show()

            progressDialog.dismiss()
            makeButtonClickable()
        }
    }

    private fun register() {

        progressDialog.setMessage("Signing up...")
        progressDialog.show()

        val email: String = tiUserEmail.editText?.text.toString()
        val password: String = tiUserPassword.editText?.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            navigateToDashboard()
            progressDialog.dismiss()
        }.addOnFailureListener { e ->
            e.printStackTrace()

            Snackbar.make(
                btnRegister,
                "Could not create new account. \n${e.message}",
                Snackbar.LENGTH_LONG
            )

            progressDialog.dismiss()
            makeButtonClickable()
        }
    }


    private fun navigateToDashboard() {

        // destroys sign in activity, so user can never back in

        Intent(this, DashboardActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            .also { intent ->
                startActivity(intent)
            }
        ActivityCompat.finishAffinity(this@AuthActivity)
    }


    private class ValidationTextWatcher(val textInputLayout: TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (textInputLayout.getId()) {
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


    private fun initViews() {
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        tiUserEmail = findViewById(R.id.tiUserEmail)
        tiUserPassword = findViewById(R.id.tiUserPassword)
    }
}