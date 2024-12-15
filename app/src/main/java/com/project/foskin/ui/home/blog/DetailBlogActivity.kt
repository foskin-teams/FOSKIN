package com.project.foskin.ui.home.blog

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ActivityDetailBlogBinding

class DetailBlogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBlogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val blogItem = intent.getParcelableExtra<BlogItem>("BLOG_ITEM")

        if (blogItem != null) {
            Glide.with(this)
                .load(blogItem.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(binding.imageHeader)

            binding.textTitle.text = blogItem.title
            binding.textDate.text = blogItem.date
            binding.textContent.text = blogItem.description
            binding.textReadTime.text = "7 min read"

            binding.buttonCancel.setOnClickListener {
                finish()
            }
        }
    }
}
