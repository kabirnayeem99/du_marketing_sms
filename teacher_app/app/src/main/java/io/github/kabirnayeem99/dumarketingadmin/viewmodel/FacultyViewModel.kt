package io.github.kabirnayeem99.dumarketingadmin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.FacultyRepository
import io.github.kabirnayeem99.dumarketingadmin.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import javax.inject.Inject


@HiltViewModel
class FacultyViewModel @Inject constructor(val repo: FacultyRepository) : ViewModel() {

    fun insertFacultyDataToDb(facultyData: FacultyData, post: String): Task<Void> =
        repo.upsertFacultyDataToDb(facultyData, post)

    fun uploadImage(imageFile: ByteArray, imageName: String): UploadTask =
        repo.uploadImage(imageFile, imageName)

    fun deleteFacultyData(facultyData: FacultyData, post: String): Task<Void>? =
        repo.deleteFacultyData(facultyData, post)

    val facultyListLiveData: LiveData<Resource<List<FacultyData>>> =
        repo.getFacultyListLiveData()
}