package io.github.kabirnayeem99.dumarketingstudent.util.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.kabirnayeem99.dumarketingstudent.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingstudent.databinding.ListItemFacultyBinding
import timber.log.Timber

class FacultyDataAdapter : RecyclerView.Adapter<FacultyDataAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemFacultyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(facultyData: FacultyData) {
            binding.tvFacultyName.text = facultyData.name
            binding.tvFacultyEmail.text = facultyData.email
            binding.tvFacultyPhone.text = facultyData.phone
            binding.tvFacultyPost.text = facultyData.post

            try {
                Glide.with(binding.root).load(facultyData.profileImageUrl)
                    .into(binding.ivProfileImage)
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }
    }


    private val diffCallBack = object : DiffUtil.ItemCallback<FacultyData>() {
        override fun areItemsTheSame(oldItem: FacultyData, newItem: FacultyData): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: FacultyData, newItem: FacultyData): Boolean {
            if (oldItem.email != newItem.email) {
                return false
            }
            if (oldItem.phone != newItem.phone) {
                return false
            }
            return true
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemFacultyBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val facultyData = differ.currentList[position]
        holder.bind(facultyData)
    }

    override fun getItemCount() = differ.currentList.size

    companion object {
        private const val TAG = "FacultyDataAdapter"
    }
}