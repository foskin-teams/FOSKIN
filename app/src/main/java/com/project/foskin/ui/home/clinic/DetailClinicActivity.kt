package com.project.foskin.ui.home.clinic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.foskin.R
import com.project.foskin.databinding.ActivityDetailClinicBinding

class DetailClinicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailClinicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val clinicItem = intent.getParcelableExtra<Clinic>("CLINIC_ITEM")

        if (clinicItem != null) {
            Glide.with(this)
                .load(clinicItem.imageResId)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(binding.ivImageClinic)

            binding.tvTitle.text = clinicItem.name ?: "No Title"
            binding.tvRating.text = "${clinicItem.rating} Out of 5.0"
            binding.tvTotalReviews.text = "${clinicItem.reviews} reviews"
            binding.tvAbout.text = clinicItem.description ?:"No Description"
            binding.tvWeekday.text = if (clinicItem.operationalHours.weekday.isNotEmpty()) {
                clinicItem.operationalHours.weekday.entries.joinToString("\n") { "${it.key}: ${it.value}" }
            } else {
                "No Weekday Hours Available"
            }

            binding.tvWeekend.text = if (clinicItem.operationalHours.weekend.isNotEmpty()) {
                clinicItem.operationalHours.weekend.entries.joinToString("\n") { "${it.key}: ${it.value}" }
            } else {
                "No Weekend Hours Available"
            }


            val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                val location = LatLng(clinicItem.detailedAddress.location.latitude, clinicItem.detailedAddress.location.longitude)
                googleMap.addMarker(MarkerOptions().position(location).title(clinicItem.name))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
