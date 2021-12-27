package io.github.kabirnayeem99.dumarketingstudent.presentation.faculty

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.common.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentFacultyBinding
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import timber.log.Timber

@AndroidEntryPoint
class FacultyFragment : BaseFragment<FragmentFacultyBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_faculty

    private val facultyViewModel: FacultyViewModel by activityViewModels()

    private val facultyDataAdapter: FacultyDataAdapter by lazy {
        FacultyDataAdapter()
    }

    override fun onCreated(savedInstanceState: Bundle?) {
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