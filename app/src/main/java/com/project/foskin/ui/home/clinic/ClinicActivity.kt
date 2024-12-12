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
        val clinicAdapter = ClinicAdapter(clinics) { clinic ->
            val intent = Intent(this, DetailClinicActivity::class.java)
            intent.putExtra("clinic", clinic)
            startActivity(intent)

        }
        binding.rvClinic.layoutManager = LinearLayoutManager(this)
        binding.rvClinic.adapter = clinicAdapter

        Log.d("ClinicActivity", "Total clinics: ${clinics.size}")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getDummyClinics(): List<Clinic> {
        return listOf(
            Clinic(
                name = "Miracle Aesthetic Clinic Malang",
                address = "Jl. Wilis No.6, Malang",
                imageResId = R.drawable.clinic_photo,
                rating = 4.8f,
                reviews = 120,
                slug = "miracle-aesthetic-malang",
                storeImage = "https://example.com/images/miracle.jpg",
                description = "A leading aesthetic clinic offering premium skin treatments.",
                operationalHours = OperationalHours(
                    opening = "09:00 AM",
                    closing = "08:00 PM",
                    weekday = mapOf("Monday" to "09:00 AM - 08:00 PM"),
                    weekend = mapOf("Saturday" to "09:00 AM - 05:00 PM", "Sunday" to "Closed")
                ),
                detailedAddress = DetailedAddress(
                    country = "Indonesia",
                    city = "Malang",
                    state = "Jawa Timur",
                    district = "Klojen",
                    streetName = "Jl. Wilis No.6",
                    location = Location(latitude = -7.981298, longitude = 112.621391),
                    maps = "https://maps.google.com/?q=Jl.+Wilis+No.6,+Malang"
                ),
                ratings = 4.8,
                totalReviews = 120
            ),
            Clinic(
                name = "Eris Clinic",
                address = "Jl. Suhat No.11, Malang",
                imageResId = R.drawable.clinic_photo,
                rating = 4.7f,
                reviews = 98,
                slug = "eris-clinic-malang",
                storeImage = "https://example.com/images/eris.jpg",
                description = "Specializes in dermatological treatments and skincare.",
                operationalHours = OperationalHours(
                    opening = "10:00 AM",
                    closing = "07:00 PM",
                    weekday = mapOf("Monday" to "10:00 AM - 07:00 PM"),
                    weekend = mapOf("Saturday" to "10:00 AM - 03:00 PM", "Sunday" to "Closed")
                ),
                detailedAddress = DetailedAddress(
                    country = "Indonesia",
                    city = "Malang",
                    state = "Jawa Timur",
                    district = "Lowokwaru",
                    streetName = "Jl. Suhat No.11",
                    location = Location(latitude = -7.946715, longitude = 112.615656),
                    maps = "https://maps.google.com/?q=Jl.+Suhat+No.11,+Malang"
                ),
                ratings = 4.7,
                totalReviews = 98
            ),
            Clinic(
                name = "Diva Beauty Clinic",
                address = "Jl. Kawi No.22, Malang",
                imageResId = R.drawable.clinic_photo,
                rating = 4.6f,
                reviews = 85,
                slug = "diva-beauty-clinic-malang",
                storeImage = "https://example.com/images/diva.jpg",
                description = "Offers beauty treatments for skin and hair.",
                operationalHours = OperationalHours(
                    opening = "08:00 AM",
                    closing = "06:00 PM",
                    weekday = mapOf("Monday" to "08:00 AM - 06:00 PM"),
                    weekend = mapOf("Saturday" to "09:00 AM - 02:00 PM", "Sunday" to "Closed")
                ),
                detailedAddress = DetailedAddress(
                    country = "Indonesia",
                    city = "Malang",
                    state = "Jawa Timur",
                    district = "Blimbing",
                    streetName = "Jl. Kawi No.22",
                    location = Location(latitude = -7.973844, longitude = 112.638726),
                    maps = "https://maps.google.com/?q=Jl.+Kawi+No.22,+Malang"
                ),
                ratings = 4.6,
                totalReviews = 85
            ),
            Clinic(
                name = "Olivia Skin Care",
                address = "Jl. Ijen No.10, Malang",
                imageResId = R.drawable.clinic_photo,
                rating = 4.5f,
                reviews = 74,
                slug = "olivia-skin-care-malang",
                storeImage = "https://example.com/images/olivia.jpg",
                description = "Personalized skincare treatments for all skin types.",
                operationalHours = OperationalHours(
                    opening = "09:00 AM",
                    closing = "07:00 PM",
                    weekday = mapOf("Monday" to "09:00 AM - 07:00 PM"),
                    weekend = mapOf("Saturday" to "10:00 AM - 04:00 PM", "Sunday" to "Closed")
                ),
                detailedAddress = DetailedAddress(
                    country = "Indonesia",
                    city = "Malang",
                    state = "Jawa Timur",
                    district = "Klojen",
                    streetName = "Jl. Ijen No.10",
                    location = Location(latitude = -7.983915, longitude = 112.623883),
                    maps = "https://maps.google.com/?q=Jl.+Ijen+No.10,+Malang"
                ),
                ratings = 4.5,
                totalReviews = 74
            ),
            Clinic(
                name = "SkinGlow Aesthetic",
                address = "Jl. Bandung No.5, Malang",
                imageResId = R.drawable.clinic_photo,
                rating = 4.9f,
                reviews = 200,
                slug = "skinglow-aesthetic-malang",
                storeImage = "https://example.com/images/skinglow.jpg",
                description = "Award-winning clinic for aesthetic treatments.",
                operationalHours = OperationalHours(
                    opening = "08:30 AM",
                    closing = "08:30 PM",
                    weekday = mapOf("Monday" to "08:30 AM - 08:30 PM"),
                    weekend = mapOf("Saturday" to "09:00 AM - 05:00 PM", "Sunday" to "Closed")
                ),
                detailedAddress = DetailedAddress(
                    country = "Indonesia",
                    city = "Malang",
                    state = "Jawa Timur",
                    district = "Lowokwaru",
                    streetName = "Jl. Bandung No.5",
                    location = Location(latitude = -7.955724, longitude = 112.621224),
                    maps = "https://maps.google.com/?q=Jl.+Bandung+No.5,+Malang"
                ),
                ratings = 4.9,
                totalReviews = 200
            ),
            Clinic(
                name = "Viva Derma Clinic",
                address = "Jl. Diponegoro No.15, Malang",
                imageResId = R.drawable.clinic_photo,
                rating = 4.8f,
                reviews = 150,
                slug = "viva-derma-clinic",
                storeImage = "https://example.com/images/viva.jpg",
                description = "Advanced dermatology and aesthetic treatments.",
                operationalHours = OperationalHours(
                    opening = "09:00 AM",
                    closing = "06:00 PM",
                    weekday = mapOf("Monday" to "09:00 AM - 06:00 PM"),
                    weekend = mapOf("Saturday" to "09:00 AM - 02:00 PM", "Sunday" to "Closed")
                ),
                detailedAddress = DetailedAddress(
                    country = "Indonesia",
                    city = "Malang",
                    state = "Jawa Timur",
                    district = "Klojen",
                    streetName = "Jl. Diponegoro No.15",
                    location = Location(latitude = -7.983610, longitude = 112.627512),
                    maps = "https://maps.google.com/?q=Jl.+Diponegoro+No.15,+Malang"
                ),
                ratings = 4.8,
                totalReviews = 150
            )
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
            val addresses = geocoder.getFromLocationName(query, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val latitude = address.latitude
                val longitude = address.longitude

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
        gMap.clear()
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
