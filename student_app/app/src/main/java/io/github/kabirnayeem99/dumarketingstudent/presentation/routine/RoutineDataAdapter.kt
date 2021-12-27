package io.github.kabirnayeem99.dumarketingstudent.presentation.routine

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kabirnayeem99.dumarketingstudent.data.dto.RoutineData
import io.github.kabirnayeem99.dumarketingstudent.databinding.ListItemRoutineBinding
import io.github.kabirnayeem99.dumarketingstudent.common.util.TimeUtilities.getHourMinuteTimeFromStringTime
import io.github.kabirnayeem99.dumarketingstudent.common.util.TimeUtilities.getMeridiemFromStringTime


class RoutineDataAdapter(var scale: ScaleAnimation) :
    RecyclerView.Adapter<RoutineDataAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ListItemRoutineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(routineData: RoutineData) {
            binding.tvSubjectName.text = routineData.className
            binding.tvTeacherName.text = routineData.facultyName
            binding.tvClassLocation.text = routineData.classLocation
            binding.tvClassTime.text = getHourMinuteTimeFromStringTime(routineData.time)
            binding.tvClassTimeMeridiem.text =
                getMeridiemFromStringTime(routineData.time).toUpperCase()

            binding.root.setOnClickListener {
                it.startAnimation(scale)
            }
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