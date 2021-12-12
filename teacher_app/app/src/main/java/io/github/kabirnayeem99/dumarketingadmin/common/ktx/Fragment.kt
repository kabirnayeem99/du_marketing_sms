package io.github.kabirnayeem99.dumarketingadmin.common.ktx

import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.awesomedialog.*
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.common.base.BaseFragment
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest


/**
 * Shows error message in a snack bar
 *
 * @param message the message that should be shown
 */
fun BaseFragment<*>.showErrorMessage(message: String) {
    val parentLayout: View = baseView
    val snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
    snackbar.animationMode = ANIMATION_MODE_SLIDE
    snackbar.show()
}


/**
 * Shows a message in a snack bar
 *
 * @param message the message that should be shown
 */
fun Fragment.showMessage(message: String) {
    try {
        val parentLayout: View? = view?.findViewById(android.R.id.content)
        val snackbar = Snackbar.make(parentLayout!!, message, Snackbar.LENGTH_LONG)
        snackbar.animationMode = ANIMATION_MODE_SLIDE
        snackbar.show()
    } catch (e: Exception) {
        context?.let { ctxt ->
            Toast.makeText(ctxt, message, Toast.LENGTH_LONG).show()
        }
    }
}


fun Fragment.showConfirmationDialog(
    title: String,
    body: String,
    iconName: Int,
    positiveText: String = "Yes",
    negativeText: String = "No",
    onPositiveClicked: () -> Unit,
) {
    AwesomeDialog.build(requireActivity())
        .title(title)
        .body(body)
        .background(R.drawable.bg_rounded_dialog)
        .icon(
            iconName,
            animateIcon = true
        )
        .onPositive(
            positiveText,
            buttonBackgroundColor = R.drawable.bg_rounded_rectangle_button_solid,
            textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context?.getColor(R.color.white) else R.color.white
        ) {
            onPositiveClicked()
        }.onNegative(
            negativeText,
            buttonBackgroundColor = R.drawable.bg_rounded_rectangle_button_solid,
            textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context?.getColor(R.color.white) else R.color.white
        )
        .show()

}

/**
 * Shows a permission disclosure dialog, mentioning all the reasons the permission is required.
 *
 * It won't be shown if the permission is already granted
 *
 * @param permissionName a human readable name of the permission
 * @param requestCode a request code to identify the permission
 * @param rationale the reason behind the permission asking
 * @param permissions a list of all the permissions that would be asked
 * @param iconName an option icon drawable to visualise the permission type
 */
fun BaseFragment<*>.showPermissionDialog(
    permissionName: String,
    requestCode: Int,
    rationale: String,
    permissions: Array<String>,
    iconName: Int = R.drawable.ic_congrts,
    onPermissionGrated: (isGranted: Boolean) -> Unit,
) {
    val hasPermissionAlready = EasyPermissions.hasPermissions(requireContext(), *permissions)
    if (hasPermissionAlready) {
        onPermissionGrated(hasPermissionAlready)
        return
    }

    AwesomeDialog.build(requireActivity())
        .title("$permissionName Permission!!!")
        .body(rationale)
        .background(R.drawable.bg_rounded_dialog)
        .icon(iconName, true)
        .onPositive(
            getString(R.string.rationale_ask_allow),
            buttonBackgroundColor = R.drawable.bg_rounded_rectangle_button_solid,
            textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context?.getColor(R.color.white) else R.color.white
        ) {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, requestCode, *permissions)
                    .setRationale(rationale)
                    .setPositiveButtonText(R.string.rationale_ask_allow)
                    .setNegativeButtonText(R.string.rationale_ask_dont)
                    .setTheme(R.style.Theme_DUMarketingAdmin)
                    .build()
            )
            onPermissionGrated(true)
        }
        .onNegative(
            getString(R.string.rationale_ask_dont),
            buttonBackgroundColor = R.drawable.bg_rounded_rectangle_button_solid,
            textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context?.getColor(R.color.white) else R.color.white
        ) {
            onPermissionGrated(false)
            showErrorMessage("Be careful!!! $rationale.")
        }

}
