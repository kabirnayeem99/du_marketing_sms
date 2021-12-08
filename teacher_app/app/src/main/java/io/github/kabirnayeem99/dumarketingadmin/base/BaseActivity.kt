package io.github.kabirnayeem99.dumarketingadmin.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import io.github.kabirnayeem99.dumarketingadmin.R

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: V
    lateinit var baseView: View

    @get:LayoutRes
    protected abstract val layout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
        baseView = binding.root
        onCreated(savedInstanceState)
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


}