package com.example.myshoppal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivitySettingsBinding
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants.GENDER
import com.example.myshoppal.utils.Constants.USER_DETAILS
import com.example.myshoppal.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private lateinit var userDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userDetails = intent.getParcelableExtra(USER_DETAILS)!!

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.app_gradient_color_background,
            )
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        GlideLoader(this).loadUserPicture(userDetails.image, binding.userPhotoImageView)

        binding.nameTextView.text = userDetails.name

        if (userDetails.gender == "") {
            binding.genderTextView.text = GENDER
        } else {
            binding.genderTextView.text = userDetails.gender
        }

        binding.emailTextView.text = userDetails.email

        if (userDetails.mobile.toString() == "0") {
            binding.mobileTextView.text = getString(R.string.default_mobile)
        } else {
            binding.mobileTextView.text = userDetails.mobile.toString()
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.settings_menu) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(USER_DETAILS, userDetails)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}