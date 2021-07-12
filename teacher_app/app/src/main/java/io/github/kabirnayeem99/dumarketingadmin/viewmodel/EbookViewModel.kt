package io.github.kabirnayeem99.dumarketingadmin.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.EbookRepository
import io.github.kabirnayeem99.dumarketingadmin.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import javax.inject.Inject


@HiltViewModel
class EbookViewModel @Inject constructor(val repo: EbookRepository) : ViewModel() {

    fun saveEbook(ebookData: EbookData): Task<Void> =
        repo.insertEbookDataToDb(ebookData)

    fun deleteEbook(ebookData: EbookData): Task<Void>? =
        repo.deleteEbookFromDb(ebookData)

    fun uploadPdf(pdfFile: Uri, pdfName: String): UploadTask =
        repo.uploadPdf(pdfFile, pdfName)

    fun getEbooks(): LiveData<Resource<List<EbookData>>> = repo.getEbooks()
}