package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentFacultyBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.ui.adapters.FacultyDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.FacultyViewModel
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FacultyFragment : BaseFragment<FragmentFacultyBinding>() {

    override val layout: Int
        get() = R.layout.fragment_faculty

    private val facultyDataAdapter: FacultyDataAdapter by lazy {
        FacultyDataAdapter()
    }

    @Inject
    lateinit var scale: ScaleAnimation


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.startAnimation(scale)

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
                    Toast.makeText(context, "Could not get the data.", Toast.LENGTH_SHORT).show()
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

    private val facultyViewModel: FacultyViewModel by viewModels()


}