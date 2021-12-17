package io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.model.GalleryData
import timber.log.Timber

class GalleryDataAdapter(private var listener: (GalleryData) -> Unit) :
    RecyclerView.Adapter<GalleryDataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivGalleryImage: ImageView = itemView.findViewById(R.id.ivGalleryImage)
        val tvImageCategory: TextView = itemView.findViewById(R.id.tvImageCategory)
        val ivBtnDelete: ImageView = itemView.findViewById(R.id.ivBtnDelete)
    }

    private val differCallback = object : DiffUtil.ItemCallback<GalleryData>() {
        override fun areItemsTheSame(
            oldItem: GalleryData,
            newItem: GalleryData
        ): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(
            oldItem: GalleryData,
            newItem: GalleryData
        ): Boolean {
            if (oldItem.imageUrl != newItem.imageUrl) {
                return false
            }

            if (oldItem.category != newItem.category) {
                return false
            }

            return true
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val listItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_gallery, parent, false)

        return ViewHolder(listItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            with(differ.currentList[position]) {

                tvImageCategory.text = category

                ivBtnDelete.setOnClickListener {
                    listener(this).also { Timber.d("onBindViewHolder: Successful") }
                }

                try {
                    Glide.with(holder.itemView).load(imageUrl).into(ivGalleryImage)
                } catch (e: Exception) {
                    Timber.e(e, "onBindViewHolder: " + e.localizedMessage)
                    e.printStackTrace()
                }

            }
        }
    }

    override fun getItemCount() = differ.currentList.size

}