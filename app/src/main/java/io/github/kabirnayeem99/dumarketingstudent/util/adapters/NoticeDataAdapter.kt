package io.github.kabirnayeem99.dumarketingstudent.util.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.kabirnayeem99.dumarketingstudent.data.vo.NoticeData
import io.github.kabirnayeem99.dumarketingstudent.databinding.ListItemNoticeDataBinding

class NoticeDataAdapter(private val listener: (NoticeData) -> Unit) :
    RecyclerView.Adapter<NoticeDataAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemNoticeDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(noticeData: NoticeData) {
            binding.tvNoticeTitle.text = noticeData.title
            binding.tvNoticeDate.text = noticeData.date
            binding.tvNoticeTime.text = noticeData.time

            binding.root.setOnClickListener {
                listener(noticeData)
            }

            try {
                Glide.with(binding.root)
                    .load(noticeData.imageUrl)
                    .into(binding.ivRecentNotice)
            } catch (e: Exception) {
                Log.e(TAG, "bind: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<NoticeData>() {
        override fun areItemsTheSame(oldItem: NoticeData, newItem: NoticeData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NoticeData, newItem: NoticeData): Boolean {
            if (oldItem.date != newItem.date) {
                return false
            }

            if (oldItem.time != newItem.time) {
                return false
            }

            if (oldItem.title != newItem.title) {
                return false
            }

            return true
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val noticeDataBinding = ListItemNoticeDataBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(noticeDataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticeData = differ.currentList[position]
        holder.bind(noticeData)
    }

    override fun getItemCount() = differ.currentList.size


    companion object {
        private const val TAG = "NoticeDataAdapter"
    }
}