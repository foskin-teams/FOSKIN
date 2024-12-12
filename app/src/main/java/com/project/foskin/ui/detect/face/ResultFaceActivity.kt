package com.project.foskin.ui.detect.face

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ActivityResultFaceBinding

class ResultFaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultFaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultFaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        showImageFront()
        showImageLeft()
        showImageRight()
    }

    private fun showImageFront() {
        val imageUriFront = imageuri.EXTRA_IMAGE_URI_FRONT
        imageUriFront?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageFront)
        }
    }

    private fun showImageLeft() {
        val imageUriLeft = imageuri.EXTRA_IMAGE_URI_LEFT
        imageUriLeft?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageLeft)
        }
    }

    private fun showImageRight() {
        val imageUriRight = imageuri.EXTRA_IMAGE_URI_RIGHT
        imageUriRight?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageRight)
        }
    }
}

