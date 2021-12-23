package io.github.kabirnayeem99.dumarketingstudent.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentFacultyBinding
import io.github.kabirnayeem99.dumarketingstudent.presentation.adapters.FacultyDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.presentation.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.common.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.presentation.viewmodel.FacultyViewModel
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import timber.log.Timber

@AndroidEntryPoint
class FacultyFragment : BaseFragment<FragmentFacultyBinding>() {

    override val layout: Int
        get() = R.layout.fragment_faculty

    private val facultyViewModel: FacultyViewModel by activityViewModels()

    private val facultyDataAdapter: FacultyDataAdapter by lazy {
        FacultyDataAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFacultyRecyclerView()
    }


    private fun setUpFacultyRecyclerView() {
        binding.rvFaculty.apply {
            adapter = facultyDataAdapter
            layoutManager = LinearLayoutManager(context)

            OverScrollDecoratorHelper.setUpOverScroll(
                this,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL
            )
        }

        facultyViewModel.getAllFacultyList().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    showSnackBar("Could not get the list of factuly members.").show()
                    Timber.e(resource.message)
                }
                is Resource.Success -> {
                    facultyDataAdapter.differ.submitList(resource.data)
                }
                else -> {
                    Timber.d("loading..")
                }
            }
        }

    }

}