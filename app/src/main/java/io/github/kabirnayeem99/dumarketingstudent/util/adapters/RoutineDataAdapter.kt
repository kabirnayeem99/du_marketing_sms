package io.github.kabirnayeem99.dumarketingstudent.util.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.dumarketingstudent.data.vo.RoutineData
import io.github.kabirnayeem99.dumarketingstudent.databinding.ListItemRoutineBinding

class RoutineDataAdapter : RecyclerView.Adapter<RoutineDataAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemRoutineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(routineData: RoutineData) {
            binding.tvSubjectName.text = routineData.className
            binding.tvTeacherName.text = routineData.facultyName
            binding.tvClassLocation.text = routineData.classLocation
            binding.tvClassTime.text = routineData.time
            binding.tvClassTimeMeridiem.text = routineData.meridian
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<RoutineData>() {
        override fun areItemsTheSame(oldItem: RoutineData, newItem: RoutineData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RoutineData, newItem: RoutineData): Boolean {
            return oldItem.time == newItem.time
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ListItemRoutineBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val routineData = differ.currentList[position]
        holder.bind(routineData)
    }

    override fun getItemCount() = differ.currentList.size
}