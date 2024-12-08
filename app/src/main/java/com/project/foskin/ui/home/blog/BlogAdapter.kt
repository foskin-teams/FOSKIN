package com.project.foskin.ui.home.blog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foskin.R

class BlogAdapter(
    private val blogList: List<BlogItem>,
    private val isHomeLayout: Boolean,
    private val onItemClicked: (BlogItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blogImage: ImageView = view.findViewById(R.id.iv_image)
        val blogDate: TextView = view.findViewById(R.id.tv_date)
        val blogTitle: TextView = view.findViewById(R.id.tv_title)
        val blogDescription: TextView = view.findViewById(R.id.tv_description)
    }

    class HorizontalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blogImage: ImageView = view.findViewById(R.id.iv_image)
        val blogDate: TextView = view.findViewById(R.id.tv_date)
        val blogTitle: TextView = view.findViewById(R.id.tv_title)
    }

    class VerticalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blogImage: ImageView = view.findViewById(R.id.iv_image)
        val blogDate: TextView = view.findViewById(R.id.tv_date)
        val blogTitle: TextView = view.findViewById(R.id.tv_title)
        val blogDescription: TextView = view.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HOME -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog_home, parent, false)
                HomeViewHolder(view)
            }
            VIEW_TYPE_HORIZONTAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog_horizontal, parent, false)
                HorizontalViewHolder(view)
            }
            else -> { // VIEW_TYPE_VERTICAL
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog_vertical, parent, false)
                VerticalViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val blogItem = blogList[position]
        when (holder) {
            is HomeViewHolder -> {
                holder.blogDate.text = blogItem.date
                holder.blogTitle.text = blogItem.title
                holder.blogDescription.text = blogItem.description
                Glide.with(holder.blogImage.context)
                    .load(blogItem.imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_error)
                    .into(holder.blogImage)
                holder.itemView.setOnClickListener { onItemClicked(blogItem) }
            }
            is HorizontalViewHolder -> {
                holder.blogDate.text = blogItem.date
                holder.blogTitle.text = blogItem.title
                Glide.with(holder.blogImage.context)
                    .load(blogItem.imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_error)
                    .into(holder.blogImage)
                holder.itemView.setOnClickListener { onItemClicked(blogItem) }
            }
            is VerticalViewHolder -> {
                holder.blogDate.text = blogItem.date
                holder.blogTitle.text = blogItem.title
                holder.blogDescription.text = blogItem.description
                Glide.with(holder.blogImage.context)
                    .load(blogItem.imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_error)
                    .into(holder.blogImage)
                holder.itemView.setOnClickListener { onItemClicked(blogItem) }
            }
        }
    }

    override fun getItemCount(): Int = blogList.size

    override fun getItemViewType(position: Int): Int {
        return if (isHomeLayout) {
            VIEW_TYPE_HOME
        } else {
            if (blogList[position].isHorizontal) {
                VIEW_TYPE_HORIZONTAL
            } else {
                VIEW_TYPE_VERTICAL
            }
        }
    }


    companion object {
        private const val VIEW_TYPE_HOME = 1
        private const val VIEW_TYPE_HORIZONTAL = 2
        private const val VIEW_TYPE_VERTICAL = 3
    }
}
