package io.github.kabirnayeem99.dumarketingstudent.util

import android.content.Context
import android.content.res.Configuration
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(msg: String): Snackbar {

    return Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        msg,
        Snackbar.LENGTH_SHORT
    )
}

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}