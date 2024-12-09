package com.project.foskin.ui.detect.face

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ActivityFaceValidationLeftBinding
import com.project.foskin.ui.detect.product.ResultScanProductActivity

class FaceValidationLeftActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceValidationLeftBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceValidationLeftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI2))

        showImage()

        binding.btnTryAgainValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionLeftActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAgreeValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionRightActivity::class.java)
//            intent.putExtra(FaceRecognitionLeftActivity.EXTRA_IMAGE_URI2, currentImageUri.toString())
            startActivity(intent)
            finish()
        }

        val backButton = findViewById<TextView>(R.id.tvBack)
        backButton.setOnClickListener {
            onBackPressed()
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
        const val EXTRA_IMAGE_URI2 = "EXTRA_IMAGE_URI2"
    }
}