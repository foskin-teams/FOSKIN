package com.project.foskin.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
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

        // Setup LinearLayouts
        val layoutEditProfile = view.findViewById<LinearLayout>(R.id.edit_profile)
        val layoutChangeNumber = view.findViewById<LinearLayout>(R.id.change_number)
        val layoutPrivacyPolicy = view.findViewById<LinearLayout>(R.id.privacy_policy)
        val layoutHelpCenter = view.findViewById<LinearLayout>(R.id.help_center)
        val layoutLogout = view.findViewById<LinearLayout>(R.id.logout)

        // Add click listeners
        layoutEditProfile.setOnClickListener {
            // Navigate to edit profile
        }

        layoutChangeNumber.setOnClickListener {
            // Navigate to change number
        }

        layoutPrivacyPolicy.setOnClickListener {
            // Navigate to privacy policy
        }

        layoutHelpCenter.setOnClickListener {
            // Navigate to help center
        }

        layoutLogout.setOnClickListener {
            showLogoutConfirmation()
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
