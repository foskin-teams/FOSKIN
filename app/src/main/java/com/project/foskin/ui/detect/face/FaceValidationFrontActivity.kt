package com.project.foskin.ui.detect.face

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ActivityFaceValidationFrontBinding
import com.project.foskin.ui.detect.product.ErrorValidateProductScanActivity
import com.project.foskin.ui.detect.product.ProductScanActivity
import com.project.foskin.ui.detect.product.ResultScanProductActivity
import com.project.foskin.ui.detect.product.ValidateProductScanActivity
import com.project.foskin.ui.detect.product.ValidateProductScanActivity.Companion

class FaceValidationFrontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceValidationFrontBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceValidationFrontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI1))

        showImage()

        binding.btnTryAgainValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionFrontActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAgreeValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionLeftActivity::class.java)
//            intent.putExtra(FaceRecognitionLeftActivity.EXTRA_IMAGE_URI1, currentImageUri.toString())
            startActivity(intent)
            finish()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Glide.with(this)
                .load(it)
                .into(binding.previewImageView)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI1 = "EXTRA_IMAGE_URI1"
    }
}