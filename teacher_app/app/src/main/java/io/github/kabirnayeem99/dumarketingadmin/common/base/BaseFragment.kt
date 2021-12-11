package io.github.kabirnayeem99.dumarketingadmin.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD

abstract class BaseFragment<V : ViewDataBinding> : Fragment() {
    @get:LayoutRes
    protected abstract val layoutRes: Int
    lateinit var baseView: View
    protected lateinit var binding: V

    lateinit var loadingIndicator: KProgressHUD

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
        loadingIndicator = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
        onCreated(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        loadingIndicator.dismiss()
    }

    protected abstract fun onCreated(savedInstanceState: Bundle?)


}