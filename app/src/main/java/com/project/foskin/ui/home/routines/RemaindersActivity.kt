package com.project.foskin.ui.home.routines

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.project.foskin.R
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RemaindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remainders)
        supportActionBar?.hide()

        val tvAddNotification: TextView = findViewById(R.id.tv_add_notification)
        val btnBack: TextView = findViewById(R.id.tvBackRemainders)

        val rvNextIntake: RecyclerView = findViewById(R.id.rv_next_intake)
        val tvNextIntakeEmpty: TextView = findViewById(R.id.tv_next_intake_empty)
        val rvPastIntake: RecyclerView = findViewById(R.id.rv_past_intake)
        val tvPastIntakeEmpty: TextView = findViewById(R.id.tv_past_intake_empty)

        tvAddNotification.setOnClickListener {
            val intent = Intent(this, AddRemaindersActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }

        // Simulasi data kosong
        val nextIntakeData = listOf<String>() // Ganti dengan data asli
        val pastIntakeData = listOf<String>() // Ganti dengan data asli

        toggleEmptyState(rvNextIntake, tvNextIntakeEmpty, nextIntakeData.isEmpty())
        toggleEmptyState(rvPastIntake, tvPastIntakeEmpty, pastIntakeData.isEmpty())
    }

    private fun toggleEmptyState(recyclerView: RecyclerView, emptyTextView: TextView, isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
        }
    }
}
