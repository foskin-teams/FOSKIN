package com.project.foskin.ui.detect.face

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import java.io.File
import java.io.FileOutputStream

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

        val backButton = findViewById<TextView>(R.id.tvBack)
        backButton.setOnClickListener {
            onBackPressed()
        }

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
                Toast.makeText(
                    this@FaceRecognitionFrontActivity,
                    "Failed to start camera.",
                    Toast.LENGTH_SHORT
                ).show()
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
                    val savedUri = Uri.fromFile(photoFile)
                    if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                        val flippedFile = flipImageHorizontally(photoFile)
                        val flippedUri = Uri.fromFile(flippedFile)

                        val intent = Intent(
                            this@FaceRecognitionFrontActivity,
                            FaceValidationFrontActivity::class.java
                        )
                        intent.putExtra(
                            FaceValidationFrontActivity.EXTRA_IMAGE_URI_FRONT,
                            flippedUri.toString()
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(
                            this@FaceRecognitionFrontActivity,
                            FaceValidationFrontActivity::class.java
                        )
                        intent.putExtra(
                            FaceValidationFrontActivity.EXTRA_IMAGE_URI_FRONT,
                            savedUri.toString()
                        )
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@FaceRecognitionFrontActivity,
                        "Failed to take picture.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "Camera error: ${exc.message}")
                }
            }
        )
    }

    private fun flipImageHorizontally(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)

        val exif = ExifInterface(file.path)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)

        val flippedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        val outputStream = FileOutputStream(file)
        flippedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return file
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
        val intent = Intent(
            Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(intent, pickImageRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageRequestCode && resultCode == RESULT_OK) {
            data?.data?.let { imageUri ->
                Log.d(TAG, "Selected Image URI: $imageUri")

                val intent = Intent(
                    this@FaceRecognitionFrontActivity,
                    FaceValidationFrontActivity::class.java
                )
                intent.putExtra(
                    FaceValidationFrontActivity.EXTRA_IMAGE_URI_FRONT,
                    imageUri.toString()
                )
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "Failed to pick image from gallery.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val TAG = "FaceRecognitionFrontActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}
