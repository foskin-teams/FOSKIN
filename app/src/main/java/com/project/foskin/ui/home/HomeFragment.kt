package com.project.foskin.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.foskin.R
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var textUserName: TextView
    private lateinit var textGreeting: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize the views
        textUserName = view.findViewById(R.id.textUserName)
        textGreeting = view.findViewById(R.id.textGreeting)

        // Update the greeting text
        updateGreeting()

        // Initialize the banner and load image
        val bannerPromoHome = view.findViewById<ImageButton>(R.id.bannerPromoHome)
        loadImageIntoButton(bannerPromoHome, "https://drive.google.com/uc?id=1DE6Rt55zuHLBsLyZqpq1XpwCWS23e0Zn")

        // Setup the formatted text
        setupFormattedText(view)

        // Setup the banner click behavior
        setupBannerPromoClick(view)

        return view
    }

    private fun updateGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        textGreeting.text = when (hour) {
            in 0..11 -> "Good Morning!"
            in 12..17 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
    }

    private fun setupFormattedText(view: View) {
        val textView = view.findViewById<TextView>(R.id.bannerPromoHomeText)
        val fullText =
            "Has your scan limit exceeded 5x a day?\nDon't worry.. you can upgrade to Premium and get additional limits now!!"
        val spannableString = SpannableString(fullText)

        // Format the text to bold the first part
        val boldStart = fullText.indexOf("Has your scan limit exceeded 5x a day?")
        val boldEnd = boldStart + "Has your scan limit exceeded 5x a day?".length
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            boldStart,
            boldEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString
    }

    private fun loadImageIntoButton(button: ImageButton, imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.holo_red_light)
            .into(button)
    }

    private fun setupBannerPromoClick(view: View) {
        val bannerButton = view.findViewById<ImageButton>(R.id.bannerPromoHome)
        bannerButton.setOnClickListener {
            Toast.makeText(context, "Banner promo di-klik!", Toast.LENGTH_SHORT).show()
            // Add any specific logic for banner click here
        }
    }
}
