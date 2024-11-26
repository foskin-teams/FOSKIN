package com.project.foskin.ui.shop

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.request.target.Target
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.project.foskin.R

class ProductRecommendedAdapter(
    private val context: Context,
    private val productItems: List<ProductItem>
) : RecyclerView.Adapter<ProductRecommendedAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_recommended, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productItems[position]

        holder.titleTextView.text = product.title
        holder.priceTextView.text = "Rp ${product.price}"
        holder.ratingTextView.text = product.rating.toString()
        holder.soldCountTextView.text = "${product.soldCount} terjual online"

        holder.progressBar.visibility = View.VISIBLE

        Glide.with(context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_error)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(holder.productImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.shopLink))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productItems.size

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImageView: ImageView = view.findViewById(R.id.image_product)
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        val titleTextView: TextView = view.findViewById(R.id.title_product)
        val priceTextView: TextView = view.findViewById(R.id.price_product)
        val ratingTextView: TextView = view.findViewById(R.id.rating_product)
        val soldCountTextView: TextView = view.findViewById(R.id.sold_count)
    }
}
