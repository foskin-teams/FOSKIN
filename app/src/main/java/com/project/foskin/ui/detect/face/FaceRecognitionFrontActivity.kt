package com.project.foskin.ui.detect.face

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.project.foskin.R
import com.project.foskin.databinding.ActivityFaceRecognitionFrontBinding
import com.project.foskin.utils.createCustomTempFile


class FaceRecognitionFrontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceRecognitionFrontBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private val pickImageRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceRecognitionFrontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.btnRotate.setOnClickListener { toggleCamera() }
        binding.btnGallery.setOnClickListener { openGallery() }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
        toggleCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(this@FaceRecognitionFrontActivity, "Failed to start camera.", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri

                    val intent = Intent(this@FaceRecognitionFrontActivity, FaceValidationFrontActivity::class.java)
                    intent.putExtra(FaceValidationFrontActivity.EXTRA_IMAGE_URI, savedUri.toString())
                    startActivity(intent)
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@FaceRecognitionFrontActivity, "Failed to take picture.", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun toggleCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, pickImageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequestCode && resultCode == RESULT_OK) {
            data?.data?.let { imageUri ->
                val intent = Intent(this@FaceRecognitionFrontActivity, FaceValidationFrontActivity::class.java)
                intent.putExtra(FaceValidationFrontActivity.EXTRA_IMAGE_URI, imageUri.toString())
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val TAG = "FaceRecognitionFrontActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}
