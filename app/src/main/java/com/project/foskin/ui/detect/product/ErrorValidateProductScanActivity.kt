package com.project.foskin.ui.detect.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.databinding.ActivityErrorValidateProductScanBinding

class ErrorValidateProductScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityErrorValidateProductScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorValidateProductScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}
