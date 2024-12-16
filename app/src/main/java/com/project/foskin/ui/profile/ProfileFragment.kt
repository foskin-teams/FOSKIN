package com.project.foskin.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.project.foskin.R
import com.project.foskin.ui.Auth.EnterWhatsappNumberActivity
import com.project.foskin.ui.home.clinic.ManageClinicActivity

class ProfileFragment : Fragment() {

    private lateinit var modeImageView: ImageView
    private lateinit var switchDarkMode: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        modeImageView = view.findViewById(R.id.mode)
        switchDarkMode = view.findViewById(R.id.switchDarkMode)

        val layoutLogout = view.findViewById<LinearLayout>(R.id.logout)
        val llPrivacyPolicy = view.findViewById<LinearLayout>(R.id.llPrivacyPolicy)
        val llWebsite = view.findViewById<LinearLayout>(R.id.llWebsite)
        val manageClinic = view.findViewById<LinearLayout>(R.id.manageClinic)

        layoutLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        llPrivacyPolicy.setOnClickListener {
            val intent = Intent(requireContext(), PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        manageClinic.setOnClickListener {
            val intent = Intent(requireContext(), ManageClinicActivity::class.java)
            startActivity(intent)
        }

        llWebsite.setOnClickListener {
            val url = "https://foskin.id/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                modeImageView.setImageResource(R.drawable.night_mode)
            } else {
                modeImageView.setImageResource(R.drawable.day_mode)
            }
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
