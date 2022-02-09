package com.example.myshoppal.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.myshoppal.R
import com.example.myshoppal.databinding.FragmentDashboardBinding
import com.example.myshoppal.models.User
import com.example.myshoppal.ui.activities.SettingsActivity
import com.example.myshoppal.utils.Constants.USER_DETAILS

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var userDetails: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        val arguments = this.arguments

        if (arguments != null) {
            userDetails = arguments.getParcelable(USER_DETAILS)!!
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.dashboard_menu, menu)

        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.dashboard_menu) {
            val intent = Intent(activity, SettingsActivity::class.java)
            intent.putExtra(USER_DETAILS, userDetails)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}