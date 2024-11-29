package com.project.foskin

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.foskin.databinding.ActivityMainBinding
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
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
            startActivity(intent)  // Launch ValidateProductScanActivity
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
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

        // Memeriksa izin kamera
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
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
            when (item.itemId) {
                R.id.navigation_home,
                R.id.navigation_shop,
                R.id.navigation_message,
                R.id.navigation_profile -> {
                    navController.navigate(item.itemId)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupFab() {
        binding.fabDetect.setOnClickListener {
            showBottomDialog()
        }
    }

    private fun showBottomDialog() {
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottomsheetlayout)
            setupDialogWindow()
        }

        // Load image from Google Drive using Glide
        val productScanButton = dialog.findViewById<ImageButton>(R.id.productScan)
        val imageUrl = "https://drive.google.com/uc?id=1wcTnrKn56Wk2V70t6-N262JS_cwDpsDD"
        Glide.with(this)
            .load(imageUrl)
            .placeholder(android.R.color.darker_gray) // Placeholder while loading
            .error(android.R.color.holo_red_light)   // Error image if loading fails
            .into(productScanButton)

        // Handle click events
        dialog.findViewById<ImageButton>(R.id.productScan)?.setOnClickListener {
            val intent = Intent(this, ProductScanActivity::class.java)
            productScanLauncher.launch(intent)
            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.imgClose)?.setOnTouchListener(
            handleDragToDismiss(dialog)
        )

        dialog.show()
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

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
