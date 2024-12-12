package com.project.foskin.ui.detect.face

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.foskin.databinding.ActivityFaceValidationRightBinding

class FaceValidationRightActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceValidationRightBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceValidationRightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI_RIGHT))

        showImage()

        binding.btnTryAgainValidate.setOnClickListener {
            val intent = Intent(this, FaceRecognitionRightActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAgreeValidate.setOnClickListener {
            val intent = Intent(this, ResultFaceActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI_RIGHT, intent.getStringExtra(EXTRA_IMAGE_URI_RIGHT))
            }
            imageuri.EXTRA_IMAGE_URI_RIGHT = currentImageUri.toString()
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
        const val EXTRA_IMAGE_URI_LEFT = "EXTRA_IMAGE_URI_LEFT"
        const val EXTRA_IMAGE_URI_RIGHT = "EXTRA_IMAGE_URI_RIGHT"
    }
}
