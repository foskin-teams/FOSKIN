package com.project.foskin.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.ui.home.blog.BlogActivity
import com.project.foskin.ui.home.blog.BlogAdapter
import com.project.foskin.ui.home.blog.BlogItem
import com.project.foskin.ui.home.blog.DetailBlogActivity
import com.project.foskin.ui.home.promo.PromoActivity
import com.project.foskin.ui.home.routines.AlarmData
import com.project.foskin.ui.home.routines.RemaindersActivity
import com.project.foskin.ui.home.routines.SharedPreferencesHelper
import com.project.foskin.ui.home.routines.timeInMillis
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    private lateinit var textUserName: TextView
    private lateinit var textGreeting: TextView
    private lateinit var notificationBadge: TextView
    private lateinit var btnNotification: ImageButton
    private lateinit var btnSeeAllBlog: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var btnSeeAllRoutine: TextView
    private lateinit var btnDetail: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        textUserName = view.findViewById(R.id.textUserName)
        textGreeting = view.findViewById(R.id.textGreeting)
        btnNotification = view.findViewById(R.id.btnNotification)
        notificationBadge = view.findViewById(R.id.notificationBadge)
        btnSeeAllBlog = view.findViewById(R.id.btnSeeAllBlog)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        btnSeeAllRoutine = view.findViewById(R.id.btnSeeAllRoutine)
        btnDetail = view.findViewById(R.id.btnDetail)

        val rvBlogHome = view.findViewById<RecyclerView>(R.id.rvBlogHome)
        rvBlogHome.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = BlogAdapter(getBlogData(), isHomeLayout = true) { blogItem ->
            val intent = Intent(context, DetailBlogActivity::class.java).apply {
                putExtra("BLOG_ITEM", blogItem)
            }
            startActivity(intent)
        }
        rvBlogHome.adapter = adapter

        updateGreeting()
        btnDetail.setOnClickListener {
            showItemDetailsDialog()
        }

        val bannerPromoHome = view.findViewById<ImageButton>(R.id.bannerPromoHome)
        loadImageIntoButton(
            bannerPromoHome,
            "https://drive.google.com/uc?id=1DE6Rt55zuHLBsLyZqpq1XpwCWS23e0Zn"
        )

        btnSeeAllRoutine.setOnClickListener {
            val intent = Intent(context, RemaindersActivity::class.java)
            startActivity(intent)
        }

        setupFormattedText(view)
        setupBannerPromoClick(view)
        setupNotificationBadge()
        setupImageViewClickListeners(view)
        setupSeeAllBlogClick()

        val savedAlarms = SharedPreferencesHelper.getAlarms(requireContext())

        if (savedAlarms.isNotEmpty()) {
            val latestAlarm = savedAlarms.last()

            view.findViewById<TextView>(R.id.showItemSkincare).text = latestAlarm.skincareItems

            updateDateTime(view)
            view.findViewById<TextView>(R.id.showTimeSkincare).text = String.format("%02d:%02d", latestAlarm.hour, latestAlarm.minute)
        }

        swipeRefreshLayout.isEnabled = false

        setupRealTimeUpdate(view)

        if (savedAlarms.isNotEmpty()) {
            updateCardRemainders(view)
        }

        return view
    }

    private fun updateDateTime(view: View) {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Calendar.getInstance().time)
        view.findViewById<TextView>(R.id.showDateSkincare).text = formattedDate
    }

    private fun getUpcomingAlarm(alarms: List<AlarmData>): AlarmData? {
        val currentTimeMillis = System.currentTimeMillis()
        return alarms
            .filter { it.timeInMillis() >= currentTimeMillis }
            .minByOrNull { it.timeInMillis() - currentTimeMillis }
    }

    private fun updateCardRemainders(view: View) {
        val savedAlarms = SharedPreferencesHelper.getAlarms(requireContext())

        val upcomingAlarm = getUpcomingAlarm(savedAlarms)

        // Update the UI elements based on the upcoming alarm
        if (upcomingAlarm != null) {
            view.findViewById<TextView>(R.id.showItemSkincare).text = upcomingAlarm.skincareItems
            view.findViewById<TextView>(R.id.showTimeSkincare).text =
                String.format("%02d:%02d", upcomingAlarm.hour, upcomingAlarm.minute)

            // Enable the Detail button since there is an upcoming alarm
            btnDetail.isEnabled = true
        } else {
            // Handle case when no upcoming alarm exists
            view.findViewById<TextView>(R.id.showItemSkincare).text = getString(R.string.no_upcoming_alarm)
            view.findViewById<TextView>(R.id.showTimeSkincare).text = getString(R.string.no_time_alarm)

            // Disable the Detail button if no upcoming alarm
            btnDetail.isEnabled = false
        }

        // Update the current date
        updateDateTime(view)
    }

    private fun showItemDetailsDialog() {
        val savedAlarms = SharedPreferencesHelper.getAlarms(requireContext())

        if (savedAlarms.isNotEmpty()) {
            val latestAlarm = savedAlarms.last()

            val scaleAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.btn_click_effect)
            btnDetail.startAnimation(scaleAnim)

            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_item_details, null)
            val textSkincareItems = dialogView.findViewById<TextView>(R.id.textSkincareSteps)
            val textDate = dialogView.findViewById<TextView>(R.id.textDate)

            val skincareSteps = latestAlarm.skincareItems.split(",")
            val formattedSteps = StringBuilder()

            skincareSteps.forEachIndexed { index, step ->
                formattedSteps.append("${index + 1}. ${step.trim()}\n")
            }

            textSkincareItems.text = formattedSteps.toString()

            val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(Calendar.getInstance().time)
            textDate.text = formattedDate

            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .create()

            alertDialog.show()
        }
    }


    private fun setupRealTimeUpdate(view: View) {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    // Memperbarui data setiap detik
                    updateDateTime(view)
                    updateCardRemainders(view)  // This will enable or disable the Detail button based on the upcoming alarm
                }
            }
        }, 0, 1000)
    }


    private fun setupSeeAllBlogClick() {
        btnSeeAllBlog.setOnClickListener {
            val intent = Intent(context, BlogActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getBlogData(): List<BlogItem> {
        val fullData = listOf(
            BlogItem(
                "The Ultimate Guide to Achieving Healthy Glowing Skin",
                "November 25, 2024",
                "Achieving glowing skin isn't just about external care; it’s a holistic process that involves nurturing your skin from within. A consistent skincare routine is the first step. This includes cleansing, toning, and moisturizing your skin every day. Cleansing removes dirt and oil, while toning restores the skin’s natural pH balance, and moisturizing ensures hydration.\n" +
                        "\n" +
                        "Diet also plays a vital role in your skin's health. Incorporating antioxidant-rich foods like berries, leafy greens, and nuts can protect your skin against free radicals that cause premature aging. Additionally, drinking at least eight glasses of water daily helps flush toxins from your system and keeps your skin plump and hydrated.\n" +
                        "\n" +
                        "Lastly, sun protection is a non-negotiable. Harmful UV rays can cause pigmentation, fine lines, and even skin cancer. Applying sunscreen with at least SPF 30 before stepping out, even on cloudy days, can prevent sun damage and maintain your skin’s youthful glow.",
                "https://drive.google.com/uc?id=1qZlC8-GeZTru30Bjr7Oey7FRAzPaonK0",
                false
            ),
            BlogItem(
                "How Stress Affects Your Skin and What to Do About It",
                "November 29, 2024",
                "Stress isn’t just a mental health issue; it manifests physically, especially on your skin. When you're stressed, your body produces cortisol, a hormone that can trigger excess oil production. This often leads to acne breakouts and an uneven skin texture.\n" +
                        "\n" +
                        "Chronic stress can also weaken your skin’s natural barrier, making it prone to irritation, dryness, and sensitivity. Conditions like eczema and psoriasis often flare up during periods of intense stress. This is why managing stress is crucial for maintaining a clear and calm complexion.\n" +
                        "\n" +
                        "Incorporating stress-relief activities like meditation, journaling, or exercise into your daily routine can help. Physical activity releases endorphins, which counteract the negative effects of cortisol. Additionally, practicing good sleep hygiene can improve both your mental state and skin health, as skin regenerates itself during deep sleep.",
                "https://drive.google.com/uc?id=1sTVQO8HApSJeVfWNVJzPIrjTNM36PDWu",
                false
            ),
            BlogItem(
                "Natural Remedies for Reducing Acne Breakouts",
                "Desember 4, 2024",
                "Acne can be frustrating, but natural remedies can complement your skincare routine to reduce breakouts. Tea tree oil is a popular natural treatment due to its antibacterial properties. Diluting it with a carrier oil and applying it to affected areas can help reduce inflammation and redness.\n" +
                        "\n" +
                        "Aloe vera is another effective remedy. Its soothing and anti-inflammatory properties not only reduce irritation but also accelerate the healing process of acne scars. Simply apply fresh aloe vera gel to your skin and leave it overnight for the best results.\n" +
                        "\n" +
                        "Proper hygiene and stress management are equally important. Stress can trigger hormonal changes that lead to breakouts, so practicing relaxation techniques like yoga or meditation can help. Keeping your hands off your face and cleaning your pillowcases regularly are also key in maintaining a breakout-free complexion.",
                "https://drive.google.com/uc?id=19f5xTPOhvutnLVr7wUQGKtuATwKjxjFn",
                false
            )
        )
        return fullData.take(3)
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
            val intent = Intent(context, RemaindersActivity::class.java)
            startActivity(intent)
        }

        ivClinic.setOnClickListener {
            Toast.makeText(context, "Clinic clicked", Toast.LENGTH_SHORT).show()
        }

        ivPromo.setOnClickListener {
            val intent = Intent(context, PromoActivity::class.java)
            startActivity(intent)
        }

        ivBlog.setOnClickListener {
            val intent = Intent(context, BlogActivity::class.java)
            startActivity(intent)
        }
    }
}