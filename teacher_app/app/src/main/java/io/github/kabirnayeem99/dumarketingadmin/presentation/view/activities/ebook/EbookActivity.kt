package io.github.kabirnayeem99.dumarketingadmin.presentation.view.activities.ebook

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.base.BaseActivity
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.databinding.ActivityEbookBinding
import io.github.kabirnayeem99.dumarketingadmin.ktx.openActivity
import io.github.kabirnayeem99.dumarketingadmin.ktx.showErrorMessage
import io.github.kabirnayeem99.dumarketingadmin.ktx.showMessage
import io.github.kabirnayeem99.dumarketingadmin.util.adapter.EbookDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel.EbookViewModel

@AndroidEntryPoint
class EbookActivity : BaseActivity<ActivityEbookBinding>() {



    override val layout: Int
        get() = R.layout.activity_ebook

    override fun onCreated(savedInstanceState: Bundle?) {

    }





    fun onFabUploadEbookClick(view: View) = openActivity(UploadEbookActivity::class.java)





}