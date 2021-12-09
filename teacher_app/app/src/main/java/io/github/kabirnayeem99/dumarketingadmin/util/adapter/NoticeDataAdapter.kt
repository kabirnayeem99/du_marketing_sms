package io.github.kabirnayeem99.dumarketingadmin.util.adapter

import android.util.Log
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
import io.github.kabirnayeem99.dumarketingadmin.data.model.NoticeData

class NoticeDataAdapter(
    // lambda
    private val listener: (NoticeData) -> Unit
) :
    RecyclerView.Adapter<NoticeDataAdapter.NoticeDataViewHolder>() {

    inner class NoticeDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNoticeTitle: TextView = itemView.findViewById(R.id.tvNoticeTitle)
        val ivDeleteNotice: ImageView = itemView.findViewById(R.id.btnDeleteNotice)
        val ivNoticeImage: ImageView = itemView.findViewById(R.id.ivNoticeImage)
    }

    private val differCallback = object : DiffUtil.ItemCallback<NoticeData>() {
        override fun areItemsTheSame(
            oldItem: NoticeData,
            newItem: NoticeData
        ): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(
            oldItem: NoticeData,
            newItem: NoticeData
        ): Boolean {
            if (oldItem.date != newItem.date) {
                return false
            }

            if (oldItem.title != newItem.title) {
                return false
            }

            return true
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeDataViewHolder {
        val listItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_notice, parent, false)

        return NoticeDataViewHolder(listItemView)
    }

    override fun onBindViewHolder(holder: NoticeDataViewHolder, position: Int) {
        holder.apply {
            with(differ.currentList[position]) {
                tvNoticeTitle.text = title

                try {
                    Glide.with(holder.itemView).load(imageUrl).into(ivNoticeImage)
                } catch (e: Exception) {
                    Log.e(TAG, "onBindViewHolder: ${e.message}")
                    e.printStackTrace()
                }

                ivDeleteNotice.setOnClickListener {
                    listener(this)
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    companion object {
        private const val TAG = "NoticeDataAdapter"
    }
}