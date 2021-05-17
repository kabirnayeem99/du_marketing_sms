package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.EbookRepository
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentEbookBinding
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.util.adapters.EbookDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.EbookViewModel
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.EbookViewModelFactory


class EbookFragment : Fragment() {

    private var _binding: FragmentEbookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEbookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ebookAdapter by lazy {
            EbookDataAdapter {
                showSnackBar("clicked ${it.title}")
            }
        }

        binding.rvEbookList.apply {
            adapter = ebookAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        ebookViewModel.getEbooks().observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Error -> {
                    showSnackBar("Could not load the ebooks")
                    Log.e(TAG, "onViewCreated: ${resource.message}")
                }
                is Resource.Success -> {
                    ebookAdapter.differ.submitList(resource.data)
                }
            }

        })
    }


    private val ebookViewModel: EbookViewModel by lazy {
        val repo = EbookRepository()
        val factory = EbookViewModelFactory(repo)
        ViewModelProvider(this, factory).get(EbookViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AboutFragment"
    }
}