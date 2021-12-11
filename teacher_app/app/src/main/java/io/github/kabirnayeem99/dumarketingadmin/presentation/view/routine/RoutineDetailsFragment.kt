package io.github.kabirnayeem99.dumarketingadmin.presentation.view.routine


import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentRoutineDetailsBinding
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.RoutineViewModel
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter.RoutineDataAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

class RoutineDetailsFragment : BaseFragment<FragmentRoutineDetailsBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_routine_details

    private var batchYear: String = ""

    private val routineViewModel: RoutineViewModel by activityViewModels()

    lateinit var navController: NavController

    private val routineDataAdapter: RoutineDataAdapter by lazy {
        RoutineDataAdapter { routineData ->
            routineViewModel.selectedRoutine = routineData
            routineViewModel.batchYear = batchYear
            navController.navigate(R.id.action_routineDetailsFragment_to_upsertRoutineFragment)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreated(savedInstanceState: Bundle?) {
        handleViews()
        setUpRecyclerView(batchYear)
    }

    private fun handleViews() {
        batchYear = routineViewModel.batchYear
        navController = findNavController()
        binding.fabAddRoutine.animateAndOnClickListener { fabAddRoutineClick() }
    }

    @ExperimentalCoroutinesApi
    private fun setUpRecyclerView(batchYear: String) {
        binding.rvRoutineList.apply {
            adapter = routineDataAdapter
            layoutManager = LinearLayoutManager(context)
        }

        routineViewModel.getRoutine(batchYear)
        routineViewModel.routines.observe(this, { routines ->
            routineDataAdapter.differ.submitList(routines)
        })

    }

    private fun fabAddRoutineClick() {
        routineViewModel.selectedRoutine = null
        routineViewModel.batchYear = batchYear
        navController.navigate(R.id.action_routineDetailsFragment_to_upsertRoutineFragment)
    }


}