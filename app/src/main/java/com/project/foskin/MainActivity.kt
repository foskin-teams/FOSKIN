package com.project.foskin

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.foskin.databinding.ActivityMainBinding
import com.project.foskin.ui.detect.face.FaceRecognitionFrontActivity
import com.project.foskin.ui.detect.face.FaceValidationFrontActivity
import com.project.foskin.ui.detect.product.ProductScanActivity
import com.project.foskin.ui.detect.product.ValidateProductScanActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val productScanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ProductScanActivity.CAMERAX_RESULT) {
            val imageUri = result.data?.getStringExtra(ProductScanActivity.EXTRA_CAMERAX_IMAGE)
            val intent = Intent(this, ValidateProductScanActivity::class.java)
            intent.putExtra(ValidateProductScanActivity.EXTRA_IMAGE_URI, imageUri)
            startActivity(intent)
        }
    }

    private val faceScanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == FaceRecognitionFrontActivity.CAMERAX_RESULT) {
            val imageUri = result.data?.getStringExtra(FaceRecognitionFrontActivity.EXTRA_CAMERAX_IMAGE)
            val intent = Intent(this, FaceValidationFrontActivity::class.java)
            intent.putExtra(FaceValidationFrontActivity.EXTRA_IMAGE_URI, imageUri)
            startActivity(intent)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        val message = if (isGranted) "Permission granted" else "Permission denied"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupFab()
        requestCameraPermissionIfNeeded()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navView = findViewById<BottomNavigationView>(R.id.navView)
        navView.background = null
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_shop,
                R.id.navigation_detect,
                R.id.navigation_message,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.navView.setOnItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true
        }
    }

    private fun setupFab() {
        binding.fabDetect.setOnClickListener {
            showBottomDialog()
        }
    }

    private fun showBottomDialog() {
        val dialog = createBottomDialog()
        setupBottomDialogContent(dialog)
        dialog.show()
    }

    private fun createBottomDialog(): Dialog {
        return Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottomsheetlayout)
            setupDialogWindow()
        }
    }

    private fun setupBottomDialogContent(dialog: Dialog) {
        val faceScanButton = dialog.findViewById<ImageButton>(R.id.faceScan)
        val productScanButton = dialog.findViewById<ImageButton>(R.id.productScan)

        loadImageIntoButton(
            faceScanButton,
            "https://drive.google.com/uc?id=1V7IygvYKqyluDopiM1pLCoOiOtp5dVzw"
        )

        faceScanButton.setOnClickListener {
            val intent = Intent(this, FaceRecognitionFrontActivity::class.java)
            faceScanLauncher.launch(intent)
            dialog.dismiss()
        }

        loadImageIntoButton(
            productScanButton,
            "https://drive.google.com/uc?id=1DRl0oax1qiqgcxT1ilLIabq4ffm6UGPK"
        )

        productScanButton.setOnClickListener {
            val intent = Intent(this, ProductScanActivity::class.java)
            productScanLauncher.launch(intent)
            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.imgClose)
            ?.setOnTouchListener(handleDragToDismiss(dialog))
    }

    private fun loadImageIntoButton(button: ImageButton, imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.holo_red_light)
            .into(button)
    }

    private fun Dialog.setupDialogWindow() {
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.DialogAnimation
            setGravity(Gravity.BOTTOM)
        }
    }

    private fun handleDragToDismiss(dialog: Dialog): View.OnTouchListener {
        var startDragY = 0f
        return View.OnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    startDragY = event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val dragDistance = event.rawY - startDragY
                    if (dragDistance > 100) {
                        dialog.dismiss()
                        true
                    } else false
                }

                else -> false
            }
        }
    }

    private fun requestCameraPermissionIfNeeded() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
