package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentFacultyBinding
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.FacultyDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.FacultyViewModel
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FacultyFragment : Fragment() {

    private var _binding: FragmentFacultyBinding? = null
    private val binding get() = _binding!!
    private val facultyDataAdapter: FacultyDataAdapter by lazy {
        FacultyDataAdapter()
    }

    @Inject
    lateinit var scale: ScaleAnimation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFacultyBinding.inflate(inflater, container, false)
        return binding.root
    }

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

        facultyViewModel.getAllFacultyList().observe(viewLifecycleOwner, { resource ->
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
        })

    }

    private val facultyViewModel: FacultyViewModel by viewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FacultyFragment"
    }
}