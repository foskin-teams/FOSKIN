package com.project.foskin.ui.detect.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.foskin.databinding.ActivityErrorValidateProductScanBinding

class ErrorValidateProductScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityErrorValidateProductScanBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorValidateProductScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Ambil URI gambar dari Intent
        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))

        // Tampilkan gambar di previewErrorImageView
        showImage()

        binding.btnTryAgainErrorValidate.setOnClickListener {
            val intent = Intent(this, ProductScanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Glide.with(this)
                .load(it)
                .into(binding.previewErrorImageView)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
    }
}

