package com.project.foskin.ui.home.routines

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R

class RemaindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remainders)
        supportActionBar?.hide()

        val tvAddNotification: TextView = findViewById(R.id.tv_add_notification)
        val btnBack: TextView = findViewById(R.id.tvBackRemainders)

        tvAddNotification.setOnClickListener {
            val intent = Intent(this, AddRemaindersActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}