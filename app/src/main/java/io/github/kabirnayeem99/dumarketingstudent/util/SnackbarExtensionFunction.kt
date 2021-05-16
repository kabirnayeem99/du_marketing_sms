package io.github.kabirnayeem99.dumarketingstudent.util

import android.R
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(msg: String): Snackbar {

    return Snackbar.make(
        requireActivity().findViewById(R.id.content),
        msg,
        Snackbar.LENGTH_SHORT
    )
}