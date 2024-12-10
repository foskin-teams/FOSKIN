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

        // Menampilkan gambar front, left, dan right
        showImageFront()
        showImageLeft()
        showImageRight()
    }

    private fun showImageFront() {
        val imageUriFront = intent.getStringExtra(EXTRA_IMAGE_URI_FRONT)
        imageUriFront?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageFront)  // Gambar depan
        }
    }

    private fun showImageLeft() {
        val imageUriLeft = intent.getStringExtra(EXTRA_IMAGE_URI_LEFT)
        imageUriLeft?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageLeft)  // Gambar kiri
        }
    }

    private fun showImageRight() {
        val imageUriRight = intent.getStringExtra(EXTRA_IMAGE_URI_RIGHT)
        imageUriRight?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageRight)  // Gambar kanan
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI_FRONT = "EXTRA_IMAGE_URI_FRONT"
        const val EXTRA_IMAGE_URI_LEFT = "EXTRA_IMAGE_URI_LEFT"
        const val EXTRA_IMAGE_URI_RIGHT = "EXTRA_IMAGE_URI_RIGHT"
    }
}

