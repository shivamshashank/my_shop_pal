package com.example.myshoppal.ui.activities

import android.os.Bundle
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginTextView.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            registerUser()
        }

    }

    private fun validateTextFields(): Boolean {

        val name: String = binding.textInputEditTextName.text.toString().trim { it <= ' ' }
        val email: String = binding.textInputEditTextEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.textInputEditTextPassword.text.toString().trim { it <= ' ' }
        val confirmPassword: String =
            binding.textInputEditTextConfirmPassword.text.toString().trim { it <= ' ' }

        when {
            name.isEmpty() -> {
                binding.textInputLayoutName.error =
                    resources.getString(R.string.err_msg_enter_name)
                return false
            }
            else -> {
                binding.textInputLayoutName.error = ""
            }
        }

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

        when {
            confirmPassword.isEmpty() -> {
                binding.textInputLayoutConfirmPassword.error =
                    resources.getString(R.string.err_msg_enter_confirm_password)
                return false
            }
            confirmPassword != password -> {
                binding.textInputLayoutConfirmPassword.error =
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch)
                return false
            }
            else -> {
                binding.textInputLayoutConfirmPassword.error = ""
            }
        }

        if (!binding.checkBoxTermsAndCondition.isChecked) {
            showSnackBar(
                resources.getString(R.string.err_msg_agree_terms_and_condition),
                true,
            )
            return false
        }

        return true

    }

    private fun registerUser() {

        if (validateTextFields()) {

            val email: String = binding.textInputEditTextEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.textInputEditTextPassword.text.toString().trim { it <= ' ' }


            showProgressDialog()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    hideProgressDialog()

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result.user!!

                        showSnackBar(resources.getString(R.string.register_success), false)
                    } else {
                        showSnackBar(task.exception?.message.toString(), true)
                    }
                }

        }
    }

}