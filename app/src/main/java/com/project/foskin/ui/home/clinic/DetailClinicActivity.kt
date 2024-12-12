package com.project.foskin.ui.home.clinic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.databinding.ActivityDetailClinicBinding

class DetailClinicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailClinicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clinic = intent.getParcelableExtra<Clinic>("clinic")

        if (clinic != null) {
            binding.tvTitle.text = clinic.name
            binding.tvAbout.text = clinic.description
            binding.tvRating.text = "${clinic.rating.toString()} Out of 5.0"
            binding.tvTotalReviews.text = "${clinic.reviews} reviews"
            binding.ivImageClinic.setImageResource(clinic.imageResId)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
