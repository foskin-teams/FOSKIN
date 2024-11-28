package com.project.foskin.ui.detect.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

        showImage()

        binding.backValidate.setOnClickListener {
            val intent = Intent(this, ProductScanActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnTryAgainValidate.setOnClickListener {
            val intent = Intent(this, ProductScanActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAgreeValidate.setOnClickListener {
            if (isImageBlur(currentImageUri)) {
                val intent = Intent(this, ErrorValidateProductScanActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ResultScanProductActivity::class.java)
                intent.putExtra(EXTRA_IMAGE_URI, currentImageUri.toString())
                startActivity(intent)
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Glide.with(this)
                .load(it)
                .into(binding.previewImageView)
        }
    }

    private fun isImageBlur(imageUri: Uri?): Boolean {
        // Logika untuk memeriksa keburaman gambar
        return false // Implementasikan sesuai dengan kebutuhan
    }

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
    }
}