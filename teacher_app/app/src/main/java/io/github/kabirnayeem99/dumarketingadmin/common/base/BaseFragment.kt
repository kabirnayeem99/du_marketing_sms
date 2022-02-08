package io.github.kabirnayeem99.dumarketingadmin.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.github.kabirnayeem99.dumarketingadmin.common.views.LoadingDialog

abstract class BaseFragment<V : ViewDataBinding> : Fragment() {
    @get:LayoutRes
    protected abstract val layoutRes: Int
    lateinit var baseView: View
    protected lateinit var binding: V

    protected val loadingIndicator: LoadingDialog by lazy(mode = LazyThreadSafetyMode.NONE) {
        LoadingDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        baseView = binding.root
        return baseView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTranslationZ(view, 100f)
        onCreated(savedInstanceState)
    }

    override fun onStop() {
        loadingIndicator.dismiss()
        super.onStop()
    }

    protected abstract fun onCreated(savedInstanceState: Bundle?)


}