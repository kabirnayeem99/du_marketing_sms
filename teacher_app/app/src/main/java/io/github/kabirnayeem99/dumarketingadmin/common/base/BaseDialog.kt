package io.github.kabirnayeem99.dumarketingadmin.common.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import io.github.kabirnayeem99.dumarketingadmin.R

abstract class BaseDialog<V : ViewDataBinding> : DialogFragment() {
    lateinit var binding: V
    lateinit var baseView: View

    @get:LayoutRes
    protected abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)

    }

    // we need to move the code in somewhere else, for now, this is only feasible solution
    // to add this transition animation in dialog
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window
            ?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, container, false)
        setUpDialogForRoundedCorner()
        baseView = binding.root
        return baseView
    }

    private fun setUpDialogForRoundedCorner() {
        dialog?.apply {
            window?.apply {
                requestFeature(Window.FEATURE_NO_TITLE)
                setLayout(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreated(savedInstanceState)
    }

    protected abstract fun onCreated(savedInstance: Bundle?)

}