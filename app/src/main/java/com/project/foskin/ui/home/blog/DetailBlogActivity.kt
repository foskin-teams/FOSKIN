package com.project.foskin.ui.home.blog

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.foskin.R

class DetailBlogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_blog)
        supportActionBar?.hide()

        val blogItem = intent.getParcelableExtra<BlogItem>("BLOG_ITEM")

        if (blogItem != null) {
            val imageHeader = findViewById<ImageView>(R.id.image_header)
            val textTitle = findViewById<TextView>(R.id.text_title)
            val textReadTime = findViewById<TextView>(R.id.text_read_time)
            val textDate = findViewById<TextView>(R.id.text_date)
            val textContent = findViewById<TextView>(R.id.text_content)
            val buttonCancel = findViewById<ImageButton>(R.id.button_cancel)

            // Set data ke UI
            Glide.with(this)
                .load(blogItem.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(imageHeader)

            textTitle.text = blogItem.title
            textDate.text = blogItem.date
            textContent.text = blogItem.description
            textReadTime.text = "7 min read"

            buttonCancel.setOnClickListener {
                finish()
            }
        }
    }
}
