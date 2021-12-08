package io.github.kabirnayeem99.dumarketingadmin.ktx

import android.view.View
import com.example.awesomedialog.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseActivity
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 * Shows error message in a snack bar
 *
 *  @param message the message that should be shown
 */
fun BaseActivity<*>.showErrorMessage(message: String) {
    val parentLayout: View = findViewById(android.R.id.content)
    val snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snackbar.show()
}


/**
 * Shows a message in a snack bar
 *
 * @param message the message that should be shown
 */
fun BaseActivity<*>.showMessage(message: String) {
    val parentLayout: View = findViewById(android.R.id.content)
    val snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snackbar.show()
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
fun BaseActivity<*>.showPermissionDialog(
    permissionName: String,
    requestCode: Int,
    rationale: String,
    permissions: Array<String>,
    iconName: Int = R.drawable.ic_fluent_question_24_filled
) {
    val hasPermissionAlready = EasyPermissions.hasPermissions(this, *permissions)
    if (hasPermissionAlready) return

    AwesomeDialog.build(this)
        .title("$permissionName Permission!!!")
        .body(rationale)
        .icon(iconName)
        .onPositive(
            getString(R.string.rationale_ask_allow),
        ) {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, requestCode, *permissions)
                    .setRationale(rationale)
                    .setPositiveButtonText(R.string.rationale_ask_allow)
                    .setNegativeButtonText(R.string.rationale_ask_dont)
                    .setTheme(R.style.Theme_DUMarketingAdmin)
                    .build()
            )
        }
        .onNegative(
            getString(R.string.rationale_ask_dont),
        ) {
            showErrorMessage("Be careful!!! $rationale.")
        }

}