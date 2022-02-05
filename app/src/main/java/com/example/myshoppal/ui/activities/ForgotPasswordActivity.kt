package com.example.myshoppal.ui.activities

import android.os.Bundle
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            submitEmail()
        }

        binding.backArrowButton.setOnClickListener {
            finish()
        }
    }

    private fun validateTextFields(): Boolean {

        val email: String = binding.textInputEditTextEmail.text.toString().trim { it <= ' ' }

        when {
            email.isEmpty() -> {
                binding.textInputLayoutEmail.error =
                    resources.getString(R.string.err_msg_enter_email)
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> {
                binding.textInputLayoutEmail.error =
                    resources.getString(R.string.err_msg_enter_invalid_email)
                return false
            }
            else -> {
                binding.textInputLayoutEmail.error = ""
            }
        }

        return true

    }

    private fun submitEmail() {

        if (validateTextFields()) {

            val email: String = binding.textInputEditTextEmail.text.toString().trim { it <= ' ' }

            showProgressDialog()

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->

                    hideProgressDialog()

                    if (task.isSuccessful) {
                        showSnackBar(resources.getString(R.string.email_sent_success), false)
                    } else {
                        showSnackBar(task.exception?.message.toString(), true)
                    }
                }

        }
    }

}