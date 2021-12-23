package io.github.kabirnayeem99.dumarketingstudent.common.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
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

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}