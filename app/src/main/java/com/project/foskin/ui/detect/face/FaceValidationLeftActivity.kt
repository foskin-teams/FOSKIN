package com.project.foskin.ui.detect.face

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.foskin.databinding.ActivityFaceValidationLeftBinding
import com.project.foskin.ui.detect.face.FaceValidationFrontActivity.Companion
import com.project.foskin.ui.detect.face.FaceValidationRightActivity.Companion.EXTRA_IMAGE_URI_RIGHT

class FaceValidationLeftActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceValidationLeftBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceValidationLeftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI_LEFT))

        showImage()

        binding.btnTryAgainValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionLeftActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAgreeValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionRightActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI_LEFT, currentImageUri?.toString())
            }
            imageuri.EXTRA_IMAGE_URI_LEFT = currentImageUri.toString()
            imageuri.printUri()
            startActivity(intent)
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
        const val EXTRA_IMAGE_URI_LEFT = "EXTRA_IMAGE_URI_LEFT"
    }
}
