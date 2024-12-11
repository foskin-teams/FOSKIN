package com.project.foskin.ui.home.clinic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.foskin.R
import com.project.foskin.databinding.ActivityClinicBinding
import java.util.Locale

class ClinicActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityClinicBinding
    private lateinit var editSearch: EditText
    private lateinit var gMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()

        editSearch = findViewById(R.id.editSearch)
        binding.btnSearchIcon.setOnClickListener {
            val query = editSearch.text.toString()
            if (query.isNotEmpty()) {
                searchLocation(query)
            } else {
                Toast.makeText(this, "Please enter a location to search.", Toast.LENGTH_SHORT).show()
            }
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

        binding.btnBack.setOnClickListener {
            finish()
        }

        val clinics = getDummyClinics()
        val clinicAdapter = ClinicAdapter(clinics)
        binding.rvClinic.layoutManager = LinearLayoutManager(this)
        binding.rvClinic.adapter = clinicAdapter

        Log.d("ClinicActivity", "Total clinics: ${clinics.size}")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getDummyClinics(): List<Clinic> {
        return listOf(
            Clinic("Miracle Aesthetic Clinic Malang", "Jl. Wilis No.6, Malang", R.drawable.clinic_photo, 4.8f, 120),
            Clinic("Eris Clinic", "Jl. Suhat No.11, Malang", R.drawable.clinic_photo, 4.7f, 98),
            Clinic("Diva Beauty Clinic", "Jl. Kawi No.22, Malang", R.drawable.clinic_photo, 4.6f, 85),
            Clinic("Olivia Skin Care", "Jl. Ijen No.10, Malang", R.drawable.clinic_photo, 4.5f, 74),
            Clinic("SkinGlow Aesthetic", "Jl. Bandung No.5, Malang", R.drawable.clinic_photo, 4.9f, 200)
        )
    }
    override fun onResume() {
        super.onResume()

        if (isLocationEnabled()) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Location is still off. Please enable it in settings.", Toast.LENGTH_SHORT).show()
            showLocationSettings()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val locality = address.locality
                        val adminArea = address.adminArea
                        val locationText = "$locality, $adminArea"

                        binding.tvCurrentLocation.text = locationText
                    } else {
                        binding.tvCurrentLocation.text = "Location name not found"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.tvCurrentLocation.text = "Error fetching location name"
                }

                moveCameraToLocation(latitude, longitude)
            } else {
                requestNewLocationData()
            }
        }.addOnFailureListener {
            binding.tvCurrentLocation.text = "Location access failed"
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val newLocation = locationResult.lastLocation
                if (newLocation != null) {
                    val latitude = newLocation.latitude
                    val longitude = newLocation.longitude

                    // Gunakan Geocoder untuk mendapatkan nama daerah
                    val geocoder = Geocoder(this@ClinicActivity, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                        if (addresses != null && addresses.isNotEmpty()) {
                            val address = addresses[0]
                            val locality = address.locality
                            val adminArea = address.adminArea
                            val locationText = "$locality, $adminArea"

                            binding.tvCurrentLocation.text = locationText
                        } else {
                            binding.tvCurrentLocation.text = "Location name not found"
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        binding.tvCurrentLocation.text = "Error fetching location name"
                    }

                    moveCameraToLocation(latitude, longitude)
                    fusedLocationClient.removeLocationUpdates(this)
                } else {
                    binding.tvCurrentLocation.text = "Unable to fetch location"
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
    }

    private fun searchLocation(query: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            // Cari alamat berdasarkan query
            val addresses = geocoder.getFromLocationName(query, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val latitude = address.latitude
                val longitude = address.longitude

                // Update tampilan lokasi
                binding.tvCurrentLocation.text = address.getAddressLine(0)
                moveCameraToLocation(latitude, longitude)
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error fetching location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCameraToLocation(latitude: Double, longitude: Double) {
        val location = LatLng(latitude, longitude)
        gMap.clear()  // Menghapus marker sebelumnya
        gMap.addMarker(MarkerOptions().position(location).title("Searched Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        val defaultLocation = LatLng(-7.9666, 112.6326)
        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Default Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
