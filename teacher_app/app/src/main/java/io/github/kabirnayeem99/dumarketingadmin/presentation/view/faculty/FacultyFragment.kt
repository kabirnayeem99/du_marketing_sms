package io.github.kabirnayeem99.dumarketingadmin.presentation.view.faculty

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.animateAndOnClickListener
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.databinding.FragmentFacultyBinding
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.adapter.FacultyAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.FacultyViewModel

@AndroidEntryPoint
class FacultyFragment : BaseFragment<FragmentFacultyBinding>() {


    private val facultyViewModel: FacultyViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_faculty

    private val facultyAdapter: FacultyAdapter by lazy {
        FacultyAdapter {
            navigateToAddFaculty(it)
        }
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        subscribeObservers()
        handleViews()
    }

    private fun subscribeObservers() {
        facultyViewModel.apply {
            facultyListObservable.observe(viewLifecycleOwner, {
                facultyAdapter.differ.submitList(it)
            })
            errorMessage.observe(viewLifecycleOwner) {
                showErrorMessage(it)
            }
            message.observe(viewLifecycleOwner) {
                showMessage(it)
            }
            isLoading.observe(viewLifecycleOwner) {
                if (it) loadingIndicator.show() else loadingIndicator.dismiss()
            }
        }
    }

    private fun handleViews() {

        facultyViewModel.getFacultyList()

        binding.rvFacultyMemberLists.apply {
            adapter = facultyAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.fabAddFaculty.animateAndOnClickListener { navigateToAddFaculty() }

    }

    private fun navigateToAddFaculty(facultyData: FacultyData? = null) {
        val isUpdate = facultyData != null
        if (isUpdate) {
            val bundle = bundleOf("faculty" to facultyData)
            findNavController().navigate(
                R.id.action_facultyFragment_to_upsertFacultyFragment,
                bundle
            )
        } else {
            findNavController().navigate(R.id.action_facultyFragment_to_upsertFacultyFragment)
        }
    }


}