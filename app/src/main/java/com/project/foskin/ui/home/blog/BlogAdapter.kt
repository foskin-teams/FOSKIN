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
    private val isHomeLayout: Boolean, // Menentukan layout (home/all)
    private val onItemClicked: (BlogItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ViewHolder untuk layout Home
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blogImage: ImageView = view.findViewById(R.id.iv_image)
        val blogDate: TextView = view.findViewById(R.id.tv_date)
        val blogTitle: TextView = view.findViewById(R.id.tv_title)
        val blogDescription: TextView = view.findViewById(R.id.tv_description)
    }

    // ViewHolder untuk layout All
    class AllViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blogImage: ImageView = view.findViewById(R.id.iv_image)
        val blogDate: TextView = view.findViewById(R.id.tv_date)
        val blogTitle: TextView = view.findViewById(R.id.tv_title)
        val blogDescription: TextView = view.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HOME) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog_home, parent, false)
            HomeViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog_all, parent, false)
            AllViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val blogItem = blogList[position]
        if (holder is HomeViewHolder) {
            // Bind data untuk layout Home
            holder.blogDate.text = blogItem.date
            holder.blogTitle.text = blogItem.title
            holder.blogDescription.text = blogItem.description
            Glide.with(holder.blogImage.context)
                .load(blogItem.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(holder.blogImage)
            holder.itemView.setOnClickListener { onItemClicked(blogItem) }
        } else if (holder is AllViewHolder) {
            // Bind data untuk layout All
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

    override fun getItemCount(): Int = blogList.size

    override fun getItemViewType(position: Int): Int {
        return if (isHomeLayout) VIEW_TYPE_HOME else VIEW_TYPE_ALL
    }

    companion object {
        private const val VIEW_TYPE_HOME = 1
        private const val VIEW_TYPE_ALL = 2
    }
}