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

    private fun showItemDetailsDialog() {
        val savedAlarms = SharedPreferencesHelper.getAlarms(requireContext())

        if (savedAlarms.isNotEmpty()) {
            val latestAlarm = savedAlarms.last()

            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_item_details, null)
            val textSkincareItems = dialogView.findViewById<TextView>(R.id.textSkincareSteps)
            val textDate = dialogView.findViewById<TextView>(R.id.textDate)

            val skincareSteps = latestAlarm.skincareItems.split(",")
            val formattedSteps = StringBuilder()

            // Create numbered list for skincare steps
            skincareSteps.forEachIndexed { index, step ->
                formattedSteps.append("${index + 1}. ${step.trim()}\n")
            }

            // Set formatted skincare steps
            textSkincareItems.text = formattedSteps.toString()

            // Format the date
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

    private fun updateCardRemainders(view: View) {
        val savedAlarms = SharedPreferencesHelper.getAlarms(requireContext())

        val upcomingAlarm = getUpcomingAlarm(savedAlarms)

        if (upcomingAlarm != null) {
            view.findViewById<TextView>(R.id.showItemSkincare).text = upcomingAlarm.skincareItems
            view.findViewById<TextView>(R.id.showTimeSkincare).text =
                String.format("%02d:%02d", upcomingAlarm.hour, upcomingAlarm.minute)
        } else {
            view.findViewById<TextView>(R.id.showItemSkincare).text = getString(R.string.no_upcoming_alarm)
            view.findViewById<TextView>(R.id.showTimeSkincare).text = getString(R.string.no_time_alarm)
        }

        updateDateTime(view)
    }

    private fun setupRealTimeUpdate(view: View) {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    // Memperbarui data setiap detik
                    updateDateTime(view)
                    updateCardRemainders(view)
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
                "https://drive.google.com/uc?id=1qZlC8-GeZTru30Bjr7Oey7FRAzPaonK0"
            ),
            BlogItem(
                "How Stress Affects Your Skin and What to Do About It",
                "November 29, 2024",
                "Stress isn’t just a mental health issue; it manifests physically, especially on your skin. When you're stressed, your body produces cortisol, a hormone that can trigger excess oil production. This often leads to acne breakouts and an uneven skin texture.\n" +
                        "\n" +
                        "Chronic stress can also weaken your skin’s natural barrier, making it prone to irritation, dryness, and sensitivity. Conditions like eczema and psoriasis often flare up during periods of intense stress. This is why managing stress is crucial for maintaining a clear and calm complexion.\n" +
                        "\n" +
                        "Incorporating stress-relief activities like meditation, journaling, or exercise into your daily routine can help. Physical activity releases endorphins, which counteract the negative effects of cortisol. Additionally, practicing good sleep hygiene can improve both your mental state and skin health, as skin regenerates itself during deep sleep.",
                "https://drive.google.com/uc?id=1sTVQO8HApSJeVfWNVJzPIrjTNM36PDWu"
            ),
            BlogItem(
                "Natural Remedies for Reducing Acne Breakouts",
                "Desember 4, 2024",
                "Acne can be frustrating, but natural remedies can complement your skincare routine to reduce breakouts. Tea tree oil is a popular natural treatment due to its antibacterial properties. Diluting it with a carrier oil and applying it to affected areas can help reduce inflammation and redness.\n" +
                        "\n" +
                        "Aloe vera is another effective remedy. Its soothing and anti-inflammatory properties not only reduce irritation but also accelerate the healing process of acne scars. Simply apply fresh aloe vera gel to your skin and leave it overnight for the best results.\n" +
                        "\n" +
                        "Proper hygiene and stress management are equally important. Stress can trigger hormonal changes that lead to breakouts, so practicing relaxation techniques like yoga or meditation can help. Keeping your hands off your face and cleaning your pillowcases regularly are also key in maintaining a breakout-free complexion.",
                "https://drive.google.com/uc?id=19f5xTPOhvutnLVr7wUQGKtuATwKjxjFn"
            ),
            BlogItem(
                "Hydration Tips for Dry and Flaky Skin",
                "November 29, 2024",
                "Dry skin can be a year-round challenge, but proper hydration is the solution. Start by using a gentle, hydrating cleanser that doesn't strip your skin of its natural oils. Follow this up with a rich moisturizer containing ingredients like hyaluronic acid, glycerin, or ceramides, which help lock in moisture.\n" +
                        "\n" +
                        "Humidifiers can make a significant difference, especially during colder months when indoor heating dries the air. By adding moisture back into the air, your skin remains supple and less prone to flakiness. Additionally, reducing your shower time and using lukewarm water instead of hot water can prevent your skin from drying out further.\n" +
                        "\n" +
                        "Don’t forget internal hydration. Drinking water, herbal teas, and eating water-rich fruits like watermelon and cucumber can improve your skin’s moisture levels from within. Avoiding alcohol and caffeine, which are known to dehydrate the body, can also keep your skin healthy.",
                "https://drive.google.com/uc?id=11rFdmIdzku4lwVdIo6OUO9ZRW_LUtgPt"
            ),
            BlogItem(
                "The Science Behind Anti-Aging Skincare",
                "November 20, 2024",
                "Anti-aging skincare is a growing field, with science-backed products and ingredients at its forefront. Retinol, a derivative of vitamin A, is one of the most effective ingredients for reducing fine lines and wrinkles. It works by accelerating cell turnover and boosting collagen production.\n" +
                        "\n" +
                        "Peptides are another powerful anti-aging ingredient. These small proteins stimulate skin cells to produce collagen and elastin, improving skin elasticity and firmness over time. Additionally, antioxidants like vitamin C protect your skin from environmental damage and help reduce dark spots, giving your skin a brighter appearance.\n" +
                        "\n" +
                        "A consistent routine with these ingredients, along with regular exfoliation, can yield remarkable results. However, sunscreen remains the cornerstone of any anti-aging regimen. UV exposure is the primary cause of premature skin aging, so applying sunscreen daily is non-negotiable.",
                "https://drive.google.com/uc?id=1gJ_L0aSG0Bkc71b-Z4StM85ja5VHzvqq"
            ),
            BlogItem(
                "The Link Between Gut Health and Clear Skin",
                "November 15, 2024",
                "The connection between gut health and skin clarity is more significant than many realize. An imbalanced gut microbiome can lead to inflammation, which often manifests as acne, redness, or rosacea on your skin.\n" +
                        "\n" +
                        "Probiotics and prebiotics play a crucial role in maintaining gut health. Yogurt, kimchi, and other fermented foods introduce beneficial bacteria, while prebiotic foods like garlic and bananas feed these bacteria, promoting a healthy gut environment.\n" +
                        "\n" +
                        "Addressing gut health doesn’t just stop at diet. Reducing stress and avoiding overuse of antibiotics are also essential. By nurturing your gut, you'll notice not only improved digestion but also a healthier, more radiant complexion.",
                "https://drive.google.com/uc?id=11p_m-sUz-C5q0BCse0NIjLMTiMzbyrX4"
            ),
            BlogItem(
                "Detoxifying Your Skin: Myths vs. Reality",
                "November 10, 2024",
                "Detoxifying the skin has become a buzzword in the beauty industry, but what does it truly mean? Contrary to popular belief, your skin doesn’t store toxins. Instead, detoxification happens primarily in the liver and kidneys.\n" +
                        "\n" +
                        "What you can do, however, is support your skin’s natural detox processes. Regular exfoliation removes dead skin cells, while clay masks can draw out impurities from your pores. Staying hydrated and eating a balanced diet rich in antioxidants also supports your skin’s ability to repair itself.\n" +
                        "\n" +
                        "Avoid falling for products claiming to “detoxify” your skin instantly. Focus on a balanced routine and healthy habits to truly enhance your skin’s natural radiance.",
                "https://drive.google.com/uc?id=14-d7VA6MPXsP-n7Y3HfGY3ryPxQy6sd_"
            ),
            BlogItem(
                "Essential Oils for Radiant and Healthy Skin",
                "November 5, 2024",
                "Essential oils have been used for centuries to improve skin health. Lavender oil is excellent for calming irritated skin and reducing redness, while rosehip oil is rich in vitamin C and fatty acids, promoting an even skin tone.\n" +
                        "\n" +
                        "Tea tree oil is another must-have, especially for acne-prone skin. Its antibacterial properties help combat breakouts without over-drying your skin. Always dilute essential oils with a carrier oil like jojoba or coconut oil to avoid irritation.\n" +
                        "\n" +
                        "Incorporating essential oils into your skincare routine can be a natural and effective way to achieve glowing skin. However, always conduct a patch test first to ensure you don’t have an allergic reaction.",
                "https://drive.google.com/uc?id=1_180BuCk37iPwSQGl-voKPKHEPvEuIN3"
            ),
            BlogItem(
                "How to Build the Perfect Nighttime Skincare Routine",
                "November 1, 2024",
                "A nighttime skincare routine is vital for skin rejuvenation. Start by removing makeup and cleansing your face to ensure all impurities are gone. Follow this with a hydrating toner to prepare your skin for the next steps.\n" +
                        "\n" +
                        "Applying serums with ingredients like hyaluronic acid or niacinamide can provide hydration and target specific concerns like dark spots or fine lines. Lock in these benefits with a nourishing night cream or facial oil.\n" +
                        "\n" +
                        "Finally, don’t forget your lips and eyes. Apply an eye cream to target puffiness or dark circles, and use a hydrating lip balm to prevent dryness overnight. With consistency, a solid nighttime routine can transform your skin.",
                "https://drive.google.com/uc?id=1tJ8pA6SslZLSVCgEfeQLxahT4uRxHzyU"
            ),
            BlogItem(
                "Top Winter Skincare Tips to Avoid Dryness",
                "October 30, 2024",
                "Winter can wreak havoc on your skin, but preventive care can make all the difference. Switch to a richer moisturizer with ingredients like shea butter or ceramides to combat the dryness caused by cold weather.\n" +
                        "\n" +
                        "Avoid long, hot showers, as they strip your skin of natural oils. Instead, opt for lukewarm water and apply moisturizer immediately after bathing to lock in hydration. Using a humidifier indoors can also help maintain your skin’s moisture levels.\n" +
                        "\n" +
                        "Lastly, don’t neglect sunscreen, even in winter. UV rays can still penetrate clouds and reflect off snow, causing damage. Protecting your skin year-round is essential for maintaining its health and vibrancy.",
                "https://drive.google.com/uc?id=1i_Q8DMaik1zTS9jC6ydMsU3JU0iuGICP"
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
            Toast.makeText(context, "Promo clicked", Toast.LENGTH_SHORT).show()
        }

        ivBlog.setOnClickListener {
            val intent = Intent(context, BlogActivity::class.java)
            startActivity(intent)
        }
    }
}