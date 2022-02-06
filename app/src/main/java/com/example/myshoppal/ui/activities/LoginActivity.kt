package com.example.myshoppal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityLoginBinding
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants.EXTRA_USER_DETAILS
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    private fun validateTextFields(): Boolean {

        val email: String = binding.textInputEditTextEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.textInputEditTextPassword.text.toString().trim { it <= ' ' }

        when {
            email.isEmpty() -> {
                binding.textInputLayoutEmail.error =
                    resources.getString(R.string.err_msg_enter_email)
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.textInputLayoutEmail.error =
                    resources.getString(R.string.err_msg_enter_invalid_email)
                return false
            }
            else -> {
                binding.textInputLayoutEmail.error = ""
            }
        }

        when {
            password.isEmpty() -> {
                binding.textInputLayoutPassword.error =
                    resources.getString(R.string.err_msg_enter_password)
                return false
            }
            password.length < 8 -> {
                binding.textInputLayoutPassword.error =
                    resources.getString(R.string.err_msg_short_password)
                return false
            }
            else -> {
                binding.textInputLayoutPassword.error = ""
            }
        }

        return true

    }

    private fun loginUser() {

        if (validateTextFields()) {

            val email: String = binding.textInputEditTextEmail.text.toString().trim { it <= ' ' }
            val password: String =
                binding.textInputEditTextPassword.text.toString().trim { it <= ' ' }

            showProgressDialog()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirestoreClass().getUserDetails(this)
                    } else {
                        hideProgressDialog()
                        showSnackBar(task.exception?.message.toString(), true)
                    }
                }

        }
    }

    fun getUserDetailsSuccess(user: User) {
        hideProgressDialog()
        showSnackBar(resources.getString(R.string.login_success), false)
        if (user.profileCompleted == 0) {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(EXTRA_USER_DETAILS, user)
            Handler(Looper.getMainLooper()).postDelayed({ startActivity(intent) }, 1000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 1000)
        }
    }

    fun getUserDetailsFailure(e: Exception) {
        hideProgressDialog()
        showSnackBar(e.message.toString(), true)
    }

}