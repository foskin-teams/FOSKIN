package com.project.foskin.ui.detect.face

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ActivityFaceValidationFrontBinding
import com.project.foskin.ui.detect.product.ValidateProductScanActivity
import com.project.foskin.ui.detect.product.ValidateProductScanActivity.Companion

class FaceValidationFrontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceValidationFrontBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFaceValidationFrontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        currentImageUri = Uri.parse(intent.getStringExtra(ValidateProductScanActivity.EXTRA_IMAGE_URI))

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