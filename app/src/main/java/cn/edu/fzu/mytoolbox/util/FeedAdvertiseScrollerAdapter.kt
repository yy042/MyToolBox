package cn.edu.fzu.mytoolbox.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.fzu.mytoolbox.R
import com.bumptech.glide.Glide

class FeedAdvertiseScrollerAdapter (private val images: List<String>) : RecyclerView.Adapter<FeedAdvertiseScrollerAdapter.SliderViewHolder>() {
    private val PAGE_COUNT = Int.MAX_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed_advertise, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val imagePosition = position % images.size
        val imageUrl = images[imagePosition]
        Glide.with(holder.itemView.context).load(imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}