package com.project.foskin.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.foskin.R
import com.project.foskin.ui.Auth.EnterWhatsappNumberActivity
import com.project.foskin.ui.home.routines.RemaindersActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val layoutLogout = view.findViewById<LinearLayout>(R.id.logout)
        val llPrivacyPolicy = view.findViewById<LinearLayout>(R.id.llPrivacyPolicy)
        val llWebsite = view.findViewById<LinearLayout>(R.id.llWebsite)
        val switchDarkMode = view.findViewById<Switch>(R.id.switchDarkMode)

        layoutLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        llPrivacyPolicy.setOnClickListener {
            val intent = Intent(requireContext(), PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        llWebsite.setOnClickListener {
            val url = "https://foskin.id/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        switchDarkMode.setOnCheckedChangeListener { _, _ ->
            Toast.makeText(requireContext(), "Next Future", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                navigateToEnterWhatsappNumberActivity()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToEnterWhatsappNumberActivity() {
        val intent = Intent(requireContext(), EnterWhatsappNumberActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
