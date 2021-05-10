package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.FacultyRepository
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentFacultyBinding
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.FacultyDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.FacultyViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.FacultyViewModelFactory
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class FacultyFragment : Fragment() {

    private var _binding: FragmentFacultyBinding? = null
    private val binding get() = _binding!!
    private val facultyDataAdapter: FacultyDataAdapter by lazy {
        FacultyDataAdapter()
    }

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
                    Log.e(TAG, "setUpFacultyRecyclerView: ${resource.message}")
                }
                is Resource.Success -> {
                    facultyDataAdapter.differ.submitList(resource.data)
                }
                else -> {
                    Log.d(TAG, "setUpFacultyRecyclerView: loading...")
                }
            }
        })

    }

    private val facultyViewModel: FacultyViewModel by lazy {
        val repo = FacultyRepository()
        val factory = FacultyViewModelFactory(repo)
        ViewModelProvider(this, factory).get(FacultyViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FacultyFragment"
    }
}