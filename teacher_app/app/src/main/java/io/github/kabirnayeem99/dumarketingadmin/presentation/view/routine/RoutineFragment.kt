package io.github.kabirnayeem99.dumarketingadmin.presentation.view.routine


import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentRoutineBinding
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.RoutineViewModel

@AndroidEntryPoint
class RoutineFragment : BaseFragment<FragmentRoutineBinding>() {

    lateinit var navController: NavController
    private val routineViewModel: RoutineViewModel by activityViewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_routine

    override fun onCreated(savedInstanceState: Bundle?) {
        navController = findNavController()
        binding.ivIconFirstYear.animateAndOnClickListener(::onMcFirstYearRoutine)
        binding.ivIconSecondYear.animateAndOnClickListener(::onMcSecondYearRoutineClick)
        binding.ivIconThirdYear.animateAndOnClickListener(::onMcThirdYearRoutine)
        binding.ivIconFourthYear.animateAndOnClickListener(::onMcFourthYearRoutineClick)
    }

    private fun onMcFirstYearRoutine(view: View) = navigateToRoutineDetailsScreen(BatchYear.First)
    private fun onMcSecondYearRoutineClick(view: View) =
        navigateToRoutineDetailsScreen(BatchYear.Second)

    private fun onMcThirdYearRoutine(view: View) = navigateToRoutineDetailsScreen(BatchYear.Third)
    private fun onMcFourthYearRoutineClick(view: View) =
        navigateToRoutineDetailsScreen(BatchYear.Fourth)


    private fun navigateToRoutineDetailsScreen(batchYear: BatchYear) {
        routineViewModel.batchYear = batchYear.value
        if (routineViewModel.batchYear.isNotEmpty())
            navController.navigate(R.id.action_fragmentRoutine_to_routineDetailsFragment)

    }

    enum class BatchYear(val value: String) {
        First("1"),
        Second("2"),
        Third("3"),
        Fourth("4"),
    }


}