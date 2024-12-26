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
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.project.foskin.R
import com.project.foskin.databinding.ActivityFaceRecognitionLeftBinding
import com.project.foskin.utils.createCustomTempFile
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

class FaceRecognitionLeftActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceRecognitionLeftBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private val pickImageRequestCode = 1001
    private var isFaceDetected = false
    private var imageAnalysis: ImageAnalysis? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceRecognitionLeftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.btnRotate.setOnClickListener { toggleCamera() }
        binding.btnGallery.setOnClickListener { openGallery() }

        setupFaceDetection()
    }

    override fun onResume() {
        super.onResume()
        startCamera()
        toggleCamera()
    }

    private fun setupFaceDetection() {
        val detectorOptions = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
            .build()

        val detector = FaceDetection.getClient(detectorOptions)

        imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val executor = Executors.newSingleThreadExecutor()
        imageAnalysis!!.setAnalyzer(
            executor
        ) { imageProxy: ImageProxy -> processImageProxy(detector, imageProxy) }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(detector: FaceDetector, imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            detector.process(inputImage)
                .addOnSuccessListener { faces: List<Face?> ->
                    binding.graphicOverlay.clear()
                    isFaceDetected = faces.isNotEmpty()
                    if (isFaceDetected) {
                        for (face in faces) {
                            val faceBox = FaceBox(
                                binding.graphicOverlay, face!!, mediaImage.cropRect
                            )
                            binding.graphicOverlay.add(faceBox)
                        }
                    }
                }
                .addOnFailureListener { e: Exception ->
                    Log.e(TAG, "Face detection failed: " + e.message)
                }
                .addOnCompleteListener { task: Task<List<Face?>?>? -> imageProxy.close() }
        } else {
            imageProxy.close()
        }
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
                    imageCapture,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                Toast.makeText(this@FaceRecognitionLeftActivity, "Failed to start camera.", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        if (!isFaceDetected) {
            Toast.makeText(this, "No face detected. Please ensure your face is visible.", Toast.LENGTH_SHORT).show()
            return
        }

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
                        val intent = Intent(this@FaceRecognitionLeftActivity, FaceValidationLeftActivity::class.java)
                        intent.putExtra(EXTRA_IMAGE_URI_LEFT, flippedUri.toString())
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@FaceRecognitionLeftActivity, FaceValidationLeftActivity::class.java)
                        intent.putExtra(EXTRA_IMAGE_URI_LEFT, savedUri.toString())
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@FaceRecognitionLeftActivity, "Failed to take picture.", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Camera error: ${exc.message}")
                }
            }
        )
    }

    private fun flipImageHorizontally(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)

        val exif = ExifInterface(file.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)

        val flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

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
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, pickImageRequestCode)
    }

    private fun cropFaceFromBitmap(bitmap: Bitmap, faces: List<Face>): Bitmap? {
        if (faces.isNotEmpty()) {
            val face = faces[0] // Kita akan mengambil wajah pertama
            val bounds = face.boundingBox

            // Memotong gambar berdasarkan koordinat bounding box wajah
            return Bitmap.createBitmap(bitmap, bounds.left, bounds.top, bounds.width(), bounds.height())
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageRequestCode && resultCode == RESULT_OK) {
            data?.data?.let { imageUri ->
                Log.d(TAG, "Selected Image URI: $imageUri")

                val inputStream = contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val inputImage = InputImage.fromBitmap(bitmap, 0)

                val detectorOptions = FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .build()
                val detector = FaceDetection.getClient(detectorOptions)

                detector.process(inputImage)
                    .addOnSuccessListener { faces ->
                        if (faces.isNotEmpty()) {
                            // Crop the face from the selected image
                            val croppedBitmap = cropFaceFromBitmap(bitmap, faces)

                            if (croppedBitmap != null) {
                                // Convert cropped bitmap back to file
                                val file = createCustomTempFile(application)
                                FileOutputStream(file).use { outputStream ->
                                    croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                }
                                val croppedUri = Uri.fromFile(file)

                                // Start FaceValidationLeftActivity with the cropped image
                                val intent = Intent(this, FaceValidationLeftActivity::class.java)
                                intent.putExtra(EXTRA_IMAGE_URI_LEFT, croppedUri.toString())
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "No face detected in the selected image.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "No face detected in the selected image.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to process the selected image.", Toast.LENGTH_SHORT).show()
                    }
            } ?: run {
                Toast.makeText(this, "Failed to pick image from gallery.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "FaceRecognitionLeftActivity"
        const val EXTRA_IMAGE_URI_LEFT = "EXTRA_IMAGE_URI_LEFT"
    }

}
