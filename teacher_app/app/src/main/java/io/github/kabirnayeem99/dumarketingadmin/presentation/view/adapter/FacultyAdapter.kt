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
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import timber.log.Timber

class FacultyAdapter(val navigateToUpdatePage: (facultyData: FacultyData) -> Unit) :
    RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder>() {

    inner class FacultyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvFacultyMemberName: TextView = itemView.findViewById(R.id.tvFacultyName)
        val tvFacultyMemberPost: TextView = itemView.findViewById(R.id.tvFacultyPost)
        val tvFacultyMemberPhone: TextView = itemView.findViewById(R.id.tvFacultyPhone)
        val tvFacultyMemberEmail: TextView = itemView.findViewById(R.id.tvFacultyEmail)
        val ivAvatarFacultyMember: ImageView = itemView.findViewById(R.id.ivProfileImage)
    }

    private var differCallback = object : DiffUtil.ItemCallback<FacultyData>() {

        override fun areItemsTheSame(oldItem: FacultyData, newItem: FacultyData): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: FacultyData, newItem: FacultyData): Boolean {
            if (oldItem.email != newItem.email) {
                return false
            }

            if (oldItem.name != newItem.name) {
                return false
            }

            if (oldItem.phone != newItem.phone) {
                return false
            }

            if (oldItem.profileImageUrl != newItem.profileImageUrl) {
                return false
            }

            return true
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_faculty, parent, false)
        return FacultyViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacultyViewHolder, position: Int) {
        holder.apply {

            with(differ.currentList[position]) {

                tvFacultyMemberName.text = name
                tvFacultyMemberPhone.text = phone
                tvFacultyMemberPost.text = post
                tvFacultyMemberEmail.text = email

                try {
                    Glide.with(holder.itemView).load(profileImageUrl).into(ivAvatarFacultyMember)
                } catch (e: Exception) {
                    Timber.e(e, "onBindViewHolder: Glide loading ${e.localizedMessage}")
                }
            }

            itemView.setOnClickListener { navigateToUpdatePage(differ.currentList[position]) }
        }
    }


    override fun getItemCount(): Int = differ.currentList.size

    companion object {
        private const val TAG = "FacultyAdapter"
    }
}