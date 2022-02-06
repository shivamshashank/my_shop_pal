package com.example.myshoppal.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityUserProfileBinding
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.Constants.EXTRA_USER_DETAILS
import com.example.myshoppal.utils.Constants.FEMALE
import com.example.myshoppal.utils.Constants.GENDER
import com.example.myshoppal.utils.Constants.MALE
import com.example.myshoppal.utils.Constants.MOBILE
import com.example.myshoppal.utils.Constants.showImageChooser
import com.example.myshoppal.utils.GlideLoader
import java.io.IOException

class UserProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    private lateinit var mobile: String
    private var mSelectedImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user: User = intent.getParcelableExtra(EXTRA_USER_DETAILS)!!

        binding.textInputEditTextName.isEnabled = false
        binding.textInputEditTextName.setText(user.name)

        binding.textInputEditTextEmail.isEnabled = false
        binding.textInputEditTextEmail.setText(user.email)


        binding.userPhotoImageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.saveButton.setOnClickListener {
            saveUserDetails()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser(this)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this).loadUserPicture(
                            mSelectedImageFileUri!!,
                            binding.userPhotoImageView
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            showSnackBar(resources.getString(R.string.image_selection_cancelled), true)
        }
    }

    private fun validateTextFields(): Boolean {
        mobile = binding.textInputEditTextMobile.text.toString().trim { it <= ' ' }

        when {
            mobile.isEmpty() -> {
                binding.textInputLayoutMobile.error =
                    resources.getString(R.string.err_msg_enter_mobile_number)
                return false
            }
            mobile.length < 10 -> {
                binding.textInputLayoutMobile.error =
                    resources.getString(R.string.err_msg_invalid_mobile_number)
                return false
            }
            else -> {
                binding.textInputLayoutMobile.error = ""
            }
        }

        return true
    }

    private fun saveUserDetails() {
        if (mSelectedImageFileUri == null) {
            showSnackBar(resources.getString(R.string.err_msg_please_select_image), true)

            return
        }

        if (validateTextFields()) {
            showProgressDialog()
            val userHashMap = HashMap<String, Any>()
            userHashMap[MOBILE] = mobile.toLong()
            userHashMap[GENDER] = if (binding.maleRadioButton.isChecked) MALE else FEMALE
            FirestoreClass().updateUserDetails(this, userHashMap)
        }

    }

    fun updateUserDetailsSuccess() {
        hideProgressDialog()
        showSnackBar(resources.getString(R.string.msg_profile_update_success), false)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }, 1000)
    }

    fun updateUserDetailsFailure() {
        hideProgressDialog()
        showSnackBar(resources.getString(R.string.err_msg_profile_update_failure), false)
    }

}