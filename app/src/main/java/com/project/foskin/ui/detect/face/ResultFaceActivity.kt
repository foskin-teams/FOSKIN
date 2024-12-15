package com.project.foskin.ui.detect.face

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ActivityResultFaceBinding
import com.project.foskin.ui.shop.ProductItem
import com.project.foskin.ui.shop.ProductRecommendedAdapter

class ResultFaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultFaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultFaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        showImageFront()
        showImageLeft()
        showImageRight()

        binding.btnBack.setOnClickListener {
            finish()
        }

        val maxScrollY = resources.displayMetrics.density * 300
        binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val translationY = if (scrollY <= maxScrollY) {
                -scrollY.toFloat() / 2
            } else {
                -maxScrollY / 2
            }
            binding.nestedScrollView.translationY = translationY
        }

        val rvRecommended = findViewById<RecyclerView>(R.id.rvRecommendationProduct)
        rvRecommended.layoutManager = GridLayoutManager(this, 2)
        val adapter2 = ProductRecommendedAdapter(this, getProductData())
        rvRecommended.adapter = adapter2
    }

    private fun showImageFront() {
        val imageUriFront = imageuri.EXTRA_IMAGE_URI_FRONT
        imageUriFront?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageFront)
        }
    }

    private fun showImageLeft() {
        val imageUriLeft = imageuri.EXTRA_IMAGE_URI_LEFT
        imageUriLeft?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageLeft)
        }
    }

    private fun showImageRight() {
        val imageUriRight = imageuri.EXTRA_IMAGE_URI_RIGHT
        imageUriRight?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageRight)
        }
    }

    private fun getProductData(): List<ProductItem> {
        return listOf(
            ProductItem(
                "https://drive.google.com/uc?id=1yIUswx4ji56HoodLKII3D1zGkEfamZNG",
                "Foskin Low pH Good Morning Celaser - 250 Ml", 50000, 4.5f, 120, "https://shopee.co.id/product-link-1"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1EYSH6AlQZAohKn9pn7sqf2oZ_p_UMUg8",
                "Hydrating Toner - Refresh and Balance Skin", 60000, 4.0f, 100, "https://shopee.co.id/product-link-2"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1vu1M5Zno-4av8uEWBv6EiwRr_X1tA_up",
                "Gentle Micellar Water - Removes Makeup and Impurities", 75000, 4.2f, 150, "https://shopee.co.id/product-link-3"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=19vDHsAvEPMeUgEOoGlm4CgbZ45ucMh6u",
                "Nourishing Essence - Boost Skin's Radiance", 80000, 4.8f, 200, "https://shopee.co.id/product-link-4"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1uvulYH7IwBPp1h4dZiypksteEJXtiA_y",
                "Vitamin C Serum - Brightening and Anti-aging", 65000, 4.4f, 130, "https://shopee.co.id/product-link-5"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1gZiUsVV7twTNFt9PZhvcb4Hh-pnLzE5B",
                "Moisturizing Sheet Mask - Hydrates and Soothes Skin", 40000, 4.1f, 110, "https://shopee.co.id/product-link-6"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1gJ2H_KznV7T8STOqcy8xtoVXTqPZe34u",
                "Repairing Ampoule - Intensive Skin Treatment", 45000, 4.3f, 140, "https://shopee.co.id/product-link-7"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1Qo1wMvbWi7JIw_15aenyacVqfetFpjd2",
                "Daily Moisturizer - Long-lasting Hydration", 25000, 4.7f, 90, "https://shopee.co.id/product-link-8"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1wNX4fKpsYUc6Vg3e4VVs-XMNs4tYZMR9",
                "Anti-aging Eye Cream - Reduces Wrinkles and Puffiness", 120000, 4.9f, 180, "https://shopee.co.id/product-link-9"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1_bp5uqsBdiwvKUPXOtsmZW98rIcqTcKg",
                "Gentle Exfoliator - Removes Dead Skin Cells", 30000, 4.6f, 160, "https://shopee.co.id/product-link-10"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1_F42W8R9Ct6lWHQ4525HToi-ZHu8CXSn",
                "Sunscreen SPF 50 - Protects from UV Rays", 55000, 4.7f, 220, "https://shopee.co.id/product-link-11"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1oJZoB4pHQ9vnBR98DU5idJd104RtyJpQ",
                "Nourishing Face Oil - Adds Glow and Softness", 70000, 4.4f, 100, "https://shopee.co.id/product-link-12"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1UuzXoUZ9-35GRHuYIGCv1iG6pQ1ZDX5-",
                "Blemish Spot Treatment - Targets Pimples", 40000, 4.5f, 90, "https://shopee.co.id/product-link-13"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1XYl4WCL1k6SyfSvuk0ZUW9jyDRwFwbpK",
                "Overnight Sleeping Mask - Skin Recovery", 85000, 4.8f, 200, "https://shopee.co.id/product-link-14"
            ),
            ProductItem(
                "https://drive.google.com/uc?id=1ql1It9etDxLJJ0w7i1p72BoOtUU9_ELU",
                "Refreshing Face Mist - Instant Hydration", 15000, 4.2f, 70, "https://shopee.co.id/product-link-15"
            )
        )
    }
}

