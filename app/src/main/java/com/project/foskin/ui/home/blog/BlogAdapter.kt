package com.project.foskin.ui.home.blog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foskin.R
import com.project.foskin.databinding.ItemBlogHomeBinding
import com.project.foskin.databinding.ItemBlogHorizontalBinding
import com.project.foskin.databinding.ItemBlogVerticalBinding

class BlogAdapter(
    private val blogList: List<BlogItem>,
    private val isHomeLayout: Boolean,
    private val onItemClicked: (BlogItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HomeViewHolder(private val binding: ItemBlogHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItem: BlogItem, onItemClicked: (BlogItem) -> Unit) {
            binding.tvDate.text = blogItem.date
            binding.tvTitle.text = blogItem.title
            binding.tvDescription.text = blogItem.description
            Glide.with(binding.ivImage.context)
                .load(blogItem.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(binding.ivImage)
            binding.root.setOnClickListener { onItemClicked(blogItem) }
        }
    }

    class HorizontalViewHolder(private val binding: ItemBlogHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItem: BlogItem, onItemClicked: (BlogItem) -> Unit) {
            binding.tvDate.text = blogItem.date
            binding.tvTitle.text = blogItem.title
            Glide.with(binding.ivImage.context)
                .load(blogItem.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(binding.ivImage)
            binding.root.setOnClickListener { onItemClicked(blogItem) }
        }
    }

    class VerticalViewHolder(private val binding: ItemBlogVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItem: BlogItem, onItemClicked: (BlogItem) -> Unit) {
            binding.tvDate.text = blogItem.date
            binding.tvTitle.text = blogItem.title
            binding.tvDescription.text = blogItem.description
            Glide.with(binding.ivImage.context)
                .load(blogItem.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error)
                .into(binding.ivImage)
            binding.root.setOnClickListener { onItemClicked(blogItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HOME -> {
                val binding = ItemBlogHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HomeViewHolder(binding)
            }
            VIEW_TYPE_HORIZONTAL -> {
                val binding = ItemBlogHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HorizontalViewHolder(binding)
            }
            else -> { // VIEW_TYPE_VERTICAL
                val binding = ItemBlogVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VerticalViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val blogItem = blogList[position]
        when (holder) {
            is HomeViewHolder -> holder.bind(blogItem, onItemClicked)
            is HorizontalViewHolder -> holder.bind(blogItem, onItemClicked)
            is VerticalViewHolder -> holder.bind(blogItem, onItemClicked)
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
