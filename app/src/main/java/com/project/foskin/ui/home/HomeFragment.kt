package com.project.foskin.ui.home

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.ui.remainders.RemaindersActivity
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var textUserName: TextView
    private lateinit var textGreeting: TextView
    private lateinit var notificationBadge: TextView
    private lateinit var btnNotification: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize the views
        textUserName = view.findViewById(R.id.textUserName)
        textGreeting = view.findViewById(R.id.textGreeting)
        btnNotification = view.findViewById(R.id.btnNotification)
        notificationBadge = view.findViewById(R.id.notificationBadge)

        updateGreeting()

        val bannerPromoHome = view.findViewById<ImageButton>(R.id.bannerPromoHome)
        loadImageIntoButton(bannerPromoHome, "https://drive.google.com/uc?id=1DE6Rt55zuHLBsLyZqpq1XpwCWS23e0Zn")

        setupFormattedText(view)

        setupBannerPromoClick(view)

//        val btnRemainders = view.findViewById<Button>(R.id.btnRemainders)
//        btnRemainders.setOnClickListener {
//            val intent = Intent(context, RemaindersActivity::class.java)
//            startActivity(intent)
//        }

        setupNotificationBadge()

        setupImageViewClickListeners(view)

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
        }
    }

    private fun setupNotificationBadge() {
        val notificationCount = 5
        if (notificationCount > 0) {
            notificationBadge.text = notificationCount.toString()
            notificationBadge.visibility = View.VISIBLE
        } else {
            notificationBadge.visibility = View.GONE
        }

        btnNotification.setOnClickListener {
            Toast.makeText(context, "Notification clicked", Toast.LENGTH_SHORT).show()
            notificationBadge.visibility = View.GONE
        }
    }

    private fun setupImageViewClickListeners(view: View) {
        val ivRoutines = view.findViewById<ImageView>(R.id.ivRoutines)
        val ivClinic = view.findViewById<ImageView>(R.id.ivClinic)
        val ivPromo = view.findViewById<ImageView>(R.id.ivPromo)
        val ivBlog = view.findViewById<ImageView>(R.id.ivBlog)

        ivRoutines.setOnClickListener {
            Toast.makeText(context, "Routines clicked", Toast.LENGTH_SHORT).show()
        }

        ivClinic.setOnClickListener {
            Toast.makeText(context, "Clinic clicked", Toast.LENGTH_SHORT).show()
        }

        ivPromo.setOnClickListener {
            Toast.makeText(context, "Promo clicked", Toast.LENGTH_SHORT).show()
        }

        ivBlog.setOnClickListener {
            Toast.makeText(context, "Blog clicked", Toast.LENGTH_SHORT).show()
        }
    }
}