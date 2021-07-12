package io.github.kabirnayeem99.dumarketingadmin.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.vo.RoutineData
import io.github.kabirnayeem99.dumarketingadmin.util.TimeUtilities.getHourMinuteTimeFromStringTime
import io.github.kabirnayeem99.dumarketingadmin.util.TimeUtilities.getMeridianFromStringTime

class RoutineDataAdapter(
    // lambda
    private val listener: (RoutineData) -> Unit
) : RecyclerView.Adapter<RoutineDataAdapter.ViewHolder>() {

    inner class ViewHolder(private val view: View) :


        RecyclerView.ViewHolder(view) {

        private val mcvRoutineListItem: MaterialCardView =
            view.findViewById(R.id.mcvRoutineListItem)
        private val tvSubjectName: TextView = view.findViewById<TextView>(R.id.tvSubjectName)
        private val tvTeacherName: TextView = view.findViewById<TextView>(R.id.tvTeacherName)
        private val tvClassLocation: TextView = view.findViewById<TextView>(R.id.tvClassLocation)
        private val tvClassTime: TextView = view.findViewById<TextView>(R.id.tvClassTime)
        private val tvClassTimeMeridiem: TextView =
            view.findViewById<TextView>(R.id.tvClassTimeMeridiem)

        fun bind(routineData: RoutineData) {
            tvSubjectName.text = routineData.className
            tvTeacherName.text = routineData.facultyName
            tvClassLocation.text = routineData.classLocation


            getHourMinuteTimeFromStringTime(routineData.time).let {
                tvClassTime.text = it
            }

            getMeridianFromStringTime(routineData.time).let {
                tvClassTimeMeridiem.text = it.toUpperCase()
            }

            mcvRoutineListItem.setOnClickListener { listener(routineData) }

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

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_routine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val routineData = differ.currentList[position]
        holder.bind(routineData)
    }

    override fun getItemCount() = differ.currentList.size
}