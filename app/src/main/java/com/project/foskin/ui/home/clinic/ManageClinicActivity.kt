package com.project.foskin.ui.home.clinic

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.project.foskin.R
import com.project.foskin.databinding.ActivityManageClinicBinding

class ManageClinicActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityManageClinicBinding
    private var selectedImageUri: Uri? = null
    private var operationalCount = 0
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.imageView.setOnClickListener {
            selectImageFromGallery()
        }

        binding.llAddOperational.setOnClickListener {
            addOperationalRow()
        }

        binding.etLatitude.addTextChangedListener { updateMap() }
        binding.etLongitude.addTextChangedListener { updateMap() }

        getCurrentLocation()
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data

                selectedImageUri?.let { uri ->
                    Glide.with(this).load(uri).into(binding.imageView)
                }
            }
        }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private fun addOperationalRow() {
        if (operationalCount < 6) {
            operationalCount++

            val newLlOperational = layoutInflater.inflate(R.layout.layout_operational, null) as LinearLayout

            binding.llContainer.addView(newLlOperational)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val initialLocation = LatLng(-34.0, 151.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))
    }

    private fun updateMap() {
        val latitude = binding.etLatitude.text.toString()
        val longitude = binding.etLongitude.text.toString()

        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
            mMap.clear()
        } else {
            try {
                val lat = latitude.toDouble()
                val lng = longitude.toDouble()

                if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
                    Toast.makeText(this, "Invalid latitude or longitude range", Toast.LENGTH_SHORT).show()
                    return
                }

                val location = LatLng(lat, lng)

                mMap.clear()
                mMap.addMarker(MarkerOptions().position(location).title("Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid latitude or longitude", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val lat = it.latitude
                val lng = it.longitude

                binding.etLatitude.setText(lat.toString())
                binding.etLongitude.setText(lng.toString())

                updateMap()
            } ?: Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
        }
    }
}
