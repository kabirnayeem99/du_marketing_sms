package io.github.kabirnayeem99.dumarketingstudent.util.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.kabirnayeem99.dumarketingstudent.data.vo.GalleryData
import io.github.kabirnayeem99.dumarketingstudent.databinding.ListItemGalleryBinding

class GalleryDataAdapter(private val listener: (GalleryData) -> Unit) :
    RecyclerView.Adapter<GalleryDataAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ListItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(galleryData: GalleryData) {
            try {
                Glide.with(binding.root).load(galleryData.imageUrl).into(binding.ivGalleryImage)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "bind: ${e.message}")
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<GalleryData>() {
        override fun areItemsTheSame(oldItem: GalleryData, newItem: GalleryData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GalleryData, newItem: GalleryData): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ListItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val galleryData = differ.currentList[position]
        holder.itemView.setOnClickListener {
            listener(galleryData)
        }
        holder.bind(galleryData)
    }

    override fun getItemCount() = differ.currentList.size

    companion object {
        private const val TAG = "GalleryDataAdapter"
    }
}