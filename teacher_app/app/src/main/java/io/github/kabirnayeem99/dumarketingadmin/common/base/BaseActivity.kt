package io.github.kabirnayeem99.dumarketingadmin.common.base

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import io.github.kabirnayeem99.dumarketingadmin.R


abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: V
    lateinit var baseView: View

    @get:LayoutRes
    protected abstract val layout: Int

    lateinit var loadingIndicator: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
        onCreated(savedInstanceState)
    }

    private fun setUpView() {
        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
        baseView = binding.root

        loadingIndicator = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)

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