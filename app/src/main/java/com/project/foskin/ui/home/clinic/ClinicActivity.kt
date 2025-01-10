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
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent
    private lateinit var listeningDialog: android.app.Dialog


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val MICROPHONE_PERMISSION_REQUEST_CODE = 101
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

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initializeSpeechRecognizer()

        binding.ibMic.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    MICROPHONE_PERMISSION_REQUEST_CODE

                )
            } else {
                startVoiceRecognition()
            }
        }
    }

    private fun showListeningDialog() {
        listeningDialog = android.app.Dialog(this).apply {
            setContentView(R.layout.dialog_listening)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
        }
        listeningDialog.show()
    }

    private fun dismissListeningDialog() {
        if (::listeningDialog.isInitialized && listeningDialog.isShowing) {
            listeningDialog.dismiss()
        }
    }


    private fun initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
    }

    private fun startVoiceRecognition() {
        showListeningDialog() // Tampilkan pop-up dialog

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                // Mungkin tambahkan indikator visual
            }

            override fun onBeginningOfSpeech() {
                // Mulai mendengarkan
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                dismissListeningDialog() // Tutup dialog saat selesai mendengarkan
            }

            override fun onError(error: Int) {
                dismissListeningDialog() // Tutup dialog saat terjadi error
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_NETWORK -> "Network error. Please try again."
                    SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Please try again."
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech input timed out. Please try again."
                    else -> "Error occurred: $error"
                }
                Toast.makeText(this@ClinicActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                dismissListeningDialog() // Tutup dialog saat hasil diterima
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0]
                    editSearch.setText(recognizedText)
                    searchLocation(recognizedText)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(speechRecognizerIntent)
    }


    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    private fun getDummyClinics(): List<Clinic> {
        return listOf(
            Clinic(
                name = "Miracle Aesthetic Clinic Malang",
                address = "Jl. Wilis No.6, Malang",
                imageResId = R.drawable.miracle,
                rating = 4.8f,
                reviews = 120,
                slug = "miracle-aesthetic-malang",
                storeImage = "https://example.com/images/miracle.jpg",
                description = "Miracle Aesthetic Clinic Malang is a premium beauty clinic that has been a trusted name in the field of aesthetic and dermatological treatments. Known for its professional approach and cutting-edge technology, the clinic offers a wide range of services including skin rejuvenation, anti-aging treatments, acne management, and body contouring. With a team of highly trained dermatologists and aesthetic experts, Miracle Aesthetic Clinic ensures a personalized experience tailored to each client's needs. The clinic also emphasizes a holistic approach to beauty, combining science and art to deliver natural and long-lasting results, making it a top choice for those seeking the best in skincare and beauty treatments.",
                operationalHours = OperationalHours(
                    opening = "09:00 AM",
                    closing = "08:00 PM",
                    weekday = mapOf(
                        "Monday" to "09:00 AM - 08:00 PM",
                        "Tuesday" to "09:00 AM - 08:00 PM",
                        "Wednesday" to "09:00 AM - 08:00 PM",
                        "Thursday" to "09:00 AM - 08:00 PM",
                        "Friday" to "09:00 AM - 08:00 PM"
                    ),
                    weekend = mapOf(
                        "Saturday" to "09:00 AM - 05:00 PM",
                        "Sunday" to "Closed"
                    )
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
                description = "Eris Clinic is a modern beauty and wellness center committed to providing high-quality aesthetic solutions in a comfortable and relaxing environment. With a focus on personalized care, the clinic offers treatments ranging from laser therapies, chemical peels, and acne solutions to advanced anti-aging and skin-tightening procedures. The expert team at Eris Clinic combines years of experience with the latest innovations in medical aesthetics to ensure safe and effective results. Whether you’re looking to address specific skin concerns or simply enhance your natural beauty, Eris Clinic is dedicated to helping you achieve your goals with professionalism and care.",
                operationalHours = OperationalHours(
                    opening = "10:00 AM",
                    closing = "07:00 PM",
                    weekday = mapOf(
                        "Monday" to "10:00 AM - 07:00 PM",
                        "Tuesday" to "10:00 AM - 07:00 PM",
                        "Wednesday" to "10:00 AM - 07:00 PM",
                        "Thursday" to "10:00 AM - 07:00 PM",
                        "Friday" to "10:00 AM - 07:00 PM"
                    ),
                    weekend = mapOf(
                        "Saturday" to "10:00 AM - 03:00 PM",
                        "Sunday" to "Closed"
                    )
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
                description = "Diva Beauty Clinic is a trusted destination for individuals seeking comprehensive beauty treatments delivered with excellence and precision. The clinic specializes in non-surgical aesthetic procedures such as facial rejuvenation, dermal fillers, body contouring, and skin whitening. With a reputation for providing outstanding client care, Diva Beauty Clinic employs the latest technology and high-quality products to achieve remarkable results. The team of certified professionals works closely with clients to design customized treatment plans that align with their beauty aspirations. Whether enhancing your skin's glow or addressing specific concerns, Diva Beauty Clinic ensures you leave feeling confident and radiant.",
                operationalHours = OperationalHours(
                    opening = "08:00 AM",
                    closing = "06:00 PM",
                    weekday = mapOf(
                        "Monday" to "08:00 AM - 06:00 PM",
                        "Tuesday" to "08:00 AM - 06:00 PM",
                        "Wednesday" to "08:00 AM - 06:00 PM",
                        "Thursday" to "08:00 AM - 06:00 PM",
                        "Friday" to "08:00 AM - 06:00 PM"
                    ),
                    weekend = mapOf(
                        "Saturday" to "09:00 AM - 02:00 PM",
                        "Sunday" to "Closed"
                    )
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
                description = "Olivia Skin Care is a premier beauty clinic that prioritizes healthy, glowing skin through state-of-the-art treatments and a client-focused approach. Offering an extensive range of skincare solutions, including deep cleansing facials, acne therapies, pigmentation correction, and anti-aging treatments, Olivia Skin Care is dedicated to helping clients achieve flawless skin. The clinic prides itself on using only safe and dermatologist-approved products, coupled with advanced techniques to deliver visible results. With a serene ambiance and a team of skilled professionals, Olivia Skin Care aims to provide not just treatments but a transformative journey toward better skin health and confidence.",
                operationalHours = OperationalHours(
                    opening = "09:00 AM",
                    closing = "07:00 PM",
                    weekday = mapOf(
                        "Monday" to "09:00 AM - 07:00 PM",
                        "Tuesday" to "09:00 AM - 07:00 PM",
                        "Wednesday" to "09:00 AM - 07:00 PM",
                        "Thursday" to "09:00 AM - 07:00 PM",
                        "Friday" to "09:00 AM - 07:00 PM"
                    ),
                    weekend = mapOf(
                        "Saturday" to "10:00 AM - 04:00 PM",
                        "Sunday" to "Closed"
                    )
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
                description = "SkinGlow Aesthetic is a renowned aesthetic clinic designed to bring out the best version of you through advanced skincare and beauty treatments. The clinic offers services such as laser resurfacing, hydrafacials, anti-aging solutions, and skin tightening treatments, tailored to address various skin concerns. With a focus on combining medical expertise and innovative technology, SkinGlow Aesthetic ensures that clients receive the highest standard of care. The team of experienced practitioners takes pride in their ability to deliver natural, radiant results while maintaining a safe and relaxing environment. SkinGlow Aesthetic is a trusted partner for anyone looking to rejuvenate their skin and boost their confidence.",
                operationalHours = OperationalHours(
                    opening = "08:30 AM",
                    closing = "08:30 PM",
                    weekday = mapOf(
                        "Monday" to "08:30 AM - 08:30 PM",
                        "Tuesday" to "08:30 AM - 08:30 PM",
                        "Wednesday" to "08:30 AM - 08:30 PM",
                        "Thursday" to "08:30 AM - 08:30 PM",
                        "Friday" to "08:30 AM - 08:30 PM"
                    ),
                    weekend = mapOf(
                        "Saturday" to "09:00 AM - 05:00 PM",
                        "Sunday" to "Closed"
                    )
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
                description = "Viva Derma Clinic is a leading aesthetic and dermatology clinic committed to enhancing natural beauty through science-backed solutions and personalized care. Specializing in advanced dermatological treatments such as microneedling, laser therapies, anti-aging procedures, and acne management, the clinic is dedicated to addressing a wide range of skin concerns effectively. The skilled team at Viva Derma Clinic combines their expertise with top-of-the-line equipment and premium products to ensure outstanding results. With a mission to promote self-confidence and well-being, Viva Derma Clinic strives to provide a supportive and comfortable space for clients to achieve their beauty and skincare goals.",
                operationalHours = OperationalHours(
                    opening = "09:00 AM",
                    closing = "06:00 PM",
                    weekday = mapOf(
                        "Monday" to "09:00 AM - 06:00 PM",
                        "Tuesday" to "09:00 AM - 06:00 PM",
                        "Wednesday" to "09:00 AM - 06:00 PM",
                        "Thursday" to "09:00 AM - 06:00 PM",
                        "Friday" to "09:00 AM - 06:00 PM"
                    ),
                    weekend = mapOf(
                        "Saturday" to "09:00 AM - 02:00 PM",
                        "Sunday" to "Closed"
                    )
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

                showClinicsAroundLocation(latitude, longitude)

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

                    // Update Clinics list after location is fetched
                    showClinicsAroundLocation(latitude, longitude)
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

                showClinicsAroundLocation(latitude, longitude)
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error fetching location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showClinicsAroundLocation(latitude: Double, longitude: Double) {
        val radius = 10000.0
        val clinics = getDummyClinics()
        val nearbyClinics = mutableListOf<Clinic>()

        gMap.clear()

        clinics.forEach { clinic ->
            val clinicLocation = LatLng(
                clinic.detailedAddress.location.latitude,
                clinic.detailedAddress.location.longitude
            )
            val results = FloatArray(1)
            Location.distanceBetween(
                latitude,
                longitude,
                clinicLocation.latitude,
                clinicLocation.longitude,
                results
            )

            if (results[0] <= radius) {
                gMap.addMarker(
                    MarkerOptions()
                        .position(clinicLocation)
                        .title(clinic.name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin))
                )
                nearbyClinics.add(clinic)
            }
        }

        updateClinicRecyclerView(nearbyClinics)
    }

    @SuppressLint("SetTextI18n")
    private fun updateClinicRecyclerView(clinics: List<Clinic>) {
        if (clinics.isEmpty()) {
            binding.rvClinic.visibility = View.GONE
            binding.tvNoClinicsMessage.visibility = View.VISIBLE
            binding.tvNoClinicsMessage.text = "There are no clinics around you"
        } else {
            binding.rvClinic.visibility = View.VISIBLE
            binding.tvNoClinicsMessage.visibility = View.GONE

            val layoutManager = LinearLayoutManager(this)
            binding.rvClinic.layoutManager = layoutManager
            val clinicAdapter = ClinicAdapter(clinics) { clinicItem ->
                val intent = Intent(this, DetailClinicActivity::class.java).apply {
                    putExtra("CLINIC_ITEM", clinicItem)
                }
                startActivity(intent)
            }
            binding.rvClinic.adapter = clinicAdapter

            clinicAdapter.notifyDataSetChanged()
        }
    }

    private fun moveCameraToLocation(latitude: Double, longitude: Double) {
        val location = LatLng(latitude, longitude)
        gMap.addMarker(MarkerOptions().position(location).title("Searched Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        val defaultLocation = LatLng(-7.9666, 112.6326)
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))

        val clinics = getDummyClinics()
        if (clinics.isNotEmpty()) {
            val builder = LatLngBounds.Builder()

            clinics.forEach { clinic ->
                val clinicLocation = LatLng(clinic.detailedAddress.location.latitude, clinic.detailedAddress.location.longitude)
                gMap.addMarker(MarkerOptions().position(clinicLocation).title(clinic.name))

                builder.include(clinicLocation)
            }

            val bounds = builder.build()
            val padding = 100
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            gMap.animateCamera(cameraUpdate)
        } else {
            Toast.makeText(this, "No clinics found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            MICROPHONE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Microphone permission granted", Toast.LENGTH_SHORT).show()
                    startVoiceRecognition()
                } else {
                    Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
