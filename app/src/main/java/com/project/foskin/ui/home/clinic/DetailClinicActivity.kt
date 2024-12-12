package com.project.foskin.ui.home.clinic

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.foskin.R
import com.project.foskin.databinding.ActivityClinicBinding
import com.project.foskin.databinding.ActivityDetailClinicBinding

class DetailClinicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailClinicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

    }
}