package io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.load
import io.github.kabirnayeem99.dumarketingadmin.databinding.ListItemEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData

class EbookDataAdapter(private var listener: (EbookData) -> Unit) :
    RecyclerView.Adapter<EbookDataAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemEbookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ebook: EbookData) {
            binding.tvBookTitle.text = ebook.name
            binding.tvAuthorName.text = ebook.authorName
            binding.ivBookCover.load(ebook.thumbnailUrl)
            binding.btnDeleteBook.animateAndOnClickListener { listener(ebook) }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<EbookData>() {
        override fun areItemsTheSame(
            oldItem: EbookData,
            newItem: EbookData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: EbookData,
            newItem: EbookData
        ): Boolean {
            if (oldItem.downloadUrl != newItem.downloadUrl) {
                return false
            }

            if (oldItem.name != newItem.name) {
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
        val binding = ListItemEbookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            holder.bind(differ.currentList[position])
        }
    }

    override fun getItemCount() = differ.currentList.size
}