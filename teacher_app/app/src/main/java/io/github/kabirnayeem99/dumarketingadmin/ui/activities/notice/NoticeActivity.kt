package io.github.kabirnayeem99.dumarketingadmin.ui.activities.notice

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingadmin.R
import io.github.kabirnayeem99.dumarketingadmin.data.vo.NoticeData
import io.github.kabirnayeem99.dumarketingadmin.ui.fragments.DelayedProgressDialog
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import io.github.kabirnayeem99.dumarketingadmin.util.adapter.NoticeDataAdapter
import io.github.kabirnayeem99.dumarketingadmin.viewmodel.NoticeViewModel

@AndroidEntryPoint
class NoticeActivity : AppCompatActivity() {

    lateinit var rvNoticeList: RecyclerView
    lateinit var noticeDataAdapter: NoticeDataAdapter
    lateinit var dialog: DelayedProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        setUpRecyclerView().also {
            fetchData()
        }

    }

    private fun fetchData() {
        noticeViewModel.noticeLiveData.observe(this, { resource ->
            when (resource) {
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show().also {
                        hideLoadingDialog()
                    }
                }
                is Resource.Success -> {
                    noticeDataAdapter.differ.submitList(resource.data).also {
                        hideLoadingDialog()
                    }
                }
                else -> {
                    Log.e(TAG, "onCreate: loading...")
                    showLoadingDialog()
                }
            }

        })
    }

    private fun setUpRecyclerView() {
        rvNoticeList = findViewById(R.id.rvNoticeList)

        showLoadingDialog()

        noticeDataAdapter = NoticeDataAdapter { noticeData ->
            showDeleteDialog { deleteNotice(noticeData) }
        }

        rvNoticeList.apply {
            adapter = noticeDataAdapter
            layoutManager = LinearLayoutManager(this@NoticeActivity)
        }
    }


    private fun showDeleteDialog(deleteNotice: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this).apply {

            setMessage("Do you want to delete this note?")

            setCancelable(true)

            setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            setPositiveButton("OK") { _, _ -> deleteNotice() }

        }

        try {
            alertDialogBuilder.create().also { alertDialog ->
                alertDialog.show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteNotice: ${e.message}")
            e.printStackTrace()
        }

    }


    private fun deleteNotice(notice: NoticeData) {

        noticeViewModel.deleteNoticeDataToDb(notice)

            ?.addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Could not delete ${notice.title}. ", Toast.LENGTH_SHORT)
                    .show()
            }

            ?.addOnSuccessListener {
                Toast.makeText(this, "Successfully deleted ${notice.title}.", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    private val noticeViewModel: NoticeViewModel by viewModels()

    private fun showLoadingDialog() {
        if (!this::dialog.isInitialized) {
            dialog = DelayedProgressDialog()
        }
        dialog.show(supportFragmentManager, TAG)
    }

    private fun hideLoadingDialog() {
        if (this::dialog.isInitialized) {
            dialog.cancel()
        }
    }

    companion object {
        private const val TAG = "DeleteNoticeActivity"
    }

    fun fabAddNoticeClick(view: View) = startActivityByClass(AddNoticeActivity::class.java)

    /**
     * Starts a new activity based on the activity class provided
     *
     * it is to simplify opening new activity for this class
     */
    private fun startActivityByClass(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }


}