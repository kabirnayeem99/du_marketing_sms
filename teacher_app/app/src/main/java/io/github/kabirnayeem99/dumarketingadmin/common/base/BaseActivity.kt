package io.github.kabirnayeem99.dumarketingadmin.common.base

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.views.LoadingDialog


abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: V
    private lateinit var baseView: View

    @get:LayoutRes
    protected abstract val layout: Int
    protected val loadingIndicator: LoadingDialog by lazy(mode = LazyThreadSafetyMode.NONE) {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
        onCreated(savedInstanceState)
    }

    private fun setUpView() {
        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
        baseView = binding.root
    }


    protected abstract fun onCreated(savedInstanceState: Bundle?)

    fun hideKeyboardFrom() {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onStop() {
        loadingIndicator.dismiss()
        super.onStop()
    }


}