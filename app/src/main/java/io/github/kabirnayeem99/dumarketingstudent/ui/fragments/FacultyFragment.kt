package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.FacultyRepository
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentFacultyBinding
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.FacultyDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.FacultyViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.FacultyViewModelFactory

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
        }

        facultyViewModel.getAllFacultyList().observe(viewLifecycleOwner, { facultyList ->
            facultyDataAdapter.differ.submitList(facultyList)
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
}