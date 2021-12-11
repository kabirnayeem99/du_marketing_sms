package io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.databinding.ListItemRecommendedBookBinding
import io.github.kabirnayeem99.dumarketingadmin.domain.data.BookOpenBook

class RecommendedBookAdapter(private var listener: (BookOpenBook) -> Unit) :
    RecyclerView.Adapter<RecommendedBookAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemRecommendedBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookOpenBook: BookOpenBook) {
            binding.tvBookName.text = bookOpenBook.name
            binding.root.animateAndOnClickListener { listener(bookOpenBook) }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<BookOpenBook>() {
        override fun areItemsTheSame(
            oldItem: BookOpenBook,
            newItem: BookOpenBook
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BookOpenBook,
            newItem: BookOpenBook
        ): Boolean {
            if (oldItem.name != newItem.name) {
                return false
            }

            if (oldItem.downloadUrl != newItem.downloadUrl) {
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
        val binding = ListItemRecommendedBookBinding.inflate(
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