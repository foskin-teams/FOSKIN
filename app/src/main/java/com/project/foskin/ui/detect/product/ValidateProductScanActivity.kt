package com.project.foskin.ui.detect.product

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ActivityValidateProductScanBinding

class ValidateProductScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityValidateProductScanBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValidateProductScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))

        val backButton = findViewById<ImageButton>(R.id.backValidate)
        backButton.setOnClickListener {
            onBackPressed()
        }

        showImage()
    }

    private fun showImage() {
        currentImageUri?.let {
            Glide.with(this)
                .load(it)
                .into(binding.previewImageView)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
    }
}