package com.example.myshoppal.ui.activities

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityMainBinding
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants.USER_DETAILS

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.app_gradient_color_background,
            )
        )

        getUserDetails()

    }

    private fun getUserDetails() {
        showProgressDialog()

        FirestoreClass().getUserDetails(this)
    }

    fun getUserDetailsSuccess(user: User) {
        hideProgressDialog()
        showSnackBar(resources.getString(R.string.msg_profile_fetch_success), false)

        val builder = NavArgumentBuilder()

        val bundle = Bundle()
        bundle.putParcelable(USER_DETAILS, user)

        val navView = binding.bottomNavigationView

        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
            R.id.dashboardFragment,
            R.id.productsFragment,
            R.id.ordersFragment
        ).build()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        builder.defaultValue = user
        navGraph.addArgument(USER_DETAILS, builder.build())
        navController.setGraph(navGraph, bundle)
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(navView, navController)

    }

    fun getUserDetailsFailure() {
        hideProgressDialog()
        showSnackBar(resources.getString(R.string.err_msg_profile_fetch_failure), true)
    }

}