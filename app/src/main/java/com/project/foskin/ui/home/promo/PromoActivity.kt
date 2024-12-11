package com.project.foskin.ui.home.promo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.foskin.R
import com.project.foskin.databinding.ActivityPromoBinding

class PromoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            finish()
        }

        val promoList = listOf(
            Promo(
                imageResId = R.drawable.promo,
                title = "Special Discount Just for You!",
                description = "Enjoy up to 30% off on all skincare products! Available this week only—don’t miss out!",
                date = "Valid until: December 16, 2024"
            ),
            Promo(
                imageResId = R.drawable.promo,
                title = "Foskin Year-End Promo!",
                description = "Celebrate the new year with up to 50% off! Get your favorite beauty products at a great price. Available on all purchases!",
                date = "Valid until: December 31, 2024"
            ),
            Promo(
                imageResId = R.drawable.promo,
                title = "Exclusive Foskin Discount",
                description = "Get 20% off your first purchase on the Foskin app. Treat yourself to the best skincare at a more affordable price!",
                date = "Valid until: December 12, 2024"
            )
        )

        val promoAdapter = PromoAdapter(promoList)

        binding.foskinPromo.apply {
            layoutManager = LinearLayoutManager(this@PromoActivity)
            adapter = promoAdapter
        }
    }
}
