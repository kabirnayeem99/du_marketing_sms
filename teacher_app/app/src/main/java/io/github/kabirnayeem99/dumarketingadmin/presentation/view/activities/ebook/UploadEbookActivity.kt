package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.ebook

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityUploadEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.presentation.view.fragments.DelayedProgressDialog
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.CONTENT_URI
import io.github.kabirnayeem99.dumarketingadmin.util.Constants.FILE_URI
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.EbookViewModel
import java.io.File

@AndroidEntryPoint
class UploadEbookActivity : BaseActivity<ActivityUploadEbookBinding>() {


    override val layout: Int
        get() = R.layout.activity_upload_ebook

    override fun onCreated(savedInstanceState: Bundle?) {
    }


}