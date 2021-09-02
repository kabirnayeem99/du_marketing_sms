package io.github.kabirnayeem99.dumarketingstudent.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.dumarketingstudent.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingstudent.databinding.ListItemEbookBinding

class EbookDataAdapter(private val listener: (EbookData) -> Unit) :
    RecyclerView.Adapter<EbookDataAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemEbookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ebookData: EbookData) {
            binding.tvEbookName.text = ebookData.title
            binding.ivDownloadButton.setOnClickListener {
                listener(ebookData)
                binding.ivDownloadButton.visibility = View.GONE
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<EbookData>() {
        override fun areItemsTheSame(
            oldItem: EbookData,
            newItem: EbookData
        ): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(
            oldItem: EbookData,
            newItem: EbookData
        ): Boolean {
            if (oldItem.pdfUrl != newItem.pdfUrl) {
                return false
            }

            if (oldItem.title != newItem.title) {
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
        val binding =
            ListItemEbookBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            holder.bind(differ.currentList[position])
        }
    }

    override fun getItemCount() = differ.currentList.size
}