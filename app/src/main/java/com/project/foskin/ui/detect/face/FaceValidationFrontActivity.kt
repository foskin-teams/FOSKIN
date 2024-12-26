package com.project.foskin.ui.detect.face

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.foskin.databinding.ActivityFaceValidationFrontBinding
import com.project.foskin.ui.detect.face.FaceValidationLeftActivity.Companion.EXTRA_IMAGE_URI_LEFT

class FaceValidationFrontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceValidationFrontBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceValidationFrontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI_FRONT))

        showImage()

        binding.btnTryAgainValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionFrontActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAgreeValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionLeftActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI_FRONT, currentImageUri?.toString())
            }
            imageuri.EXTRA_IMAGE_URI_FRONT = currentImageUri.toString()
            imageuri.printUri()
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
        const val EXTRA_IMAGE_URI_FRONT = "EXTRA_IMAGE_URI_FRONT"
    }
}
