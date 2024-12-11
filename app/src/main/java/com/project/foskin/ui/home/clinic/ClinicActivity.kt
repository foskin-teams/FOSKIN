package com.project.foskin.ui.home.clinic

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.foskin.R
import com.project.foskin.databinding.ActivityClinicBinding

class ClinicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClinicBinding
    private lateinit var editSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        editSearch = findViewById(R.id.editSearch)
        binding.btnSearchIcon.setOnClickListener {
            val query = editSearch.text.toString()
            Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
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
    }

    private fun getDummyClinics(): List<Clinic> {
        return listOf(
            Clinic("Miracle Aesthetic Clinic Malang", "Jl. Wilis No.6, Malang", R.drawable.clinic_photo, 4.8f, 120),
            Clinic("Eris Clinic", "Jl. Suhat No.11, Malang", R.drawable.clinic_photo, 4.7f, 98),
            Clinic("Diva Beauty Clinic", "Jl. Kawi No.22, Malang", R.drawable.clinic_photo, 4.6f, 85),
            Clinic("Olivia Skin Care", "Jl. Ijen No.10, Malang", R.drawable.clinic_photo, 4.5f, 74),
            Clinic("SkinGlow Aesthetic", "Jl. Bandung No.5, Malang", R.drawable.clinic_photo, 4.9f, 200),
            Clinic("GlowDerm Clinic", "Jl. Tidar No.7, Malang", R.drawable.clinic_photo, 4.6f, 140),
            Clinic("Radiance Clinic", "Jl. Veteran No.9, Malang", R.drawable.clinic_photo, 4.8f, 110),
            Clinic("Aesthetic Pro", "Jl. Sudirman No.3, Malang", R.drawable.clinic_photo, 4.7f, 90),
            Clinic("Elite Skin Care", "Jl. Merdeka No.1, Malang", R.drawable.clinic_photo, 4.5f, 100),
            Clinic("Prime Dermatology", "Jl. Basuki Rahmat No.15, Malang", R.drawable.clinic_photo, 4.7f, 115)
        )
    }
}