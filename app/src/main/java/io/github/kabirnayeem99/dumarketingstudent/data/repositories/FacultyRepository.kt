package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.FacultyData.Companion.toFacultyDataList
import io.github.kabirnayeem99.dumarketingstudent.util.Constants.FACULTY_STORAGE_PATH
import io.github.kabirnayeem99.dumarketingstudent.util.enums.FacultyPosts

class FacultyRepository {

    private val db = FirebaseFirestore.getInstance()
    private val facultyDataListLiveData = MutableLiveData<List<FacultyData>>()

    // this mess needs to be cleaned soon
    fun getAllFacultyList(): LiveData<List<FacultyData>> {

        val facultyDataList = mutableListOf<FacultyData>()

        val refChairman =
            db.collection(FACULTY_STORAGE_PATH).document(FacultyPosts.CHAIRMAN.values)
                .collection("members_list")

        val refProfessor = db.collection(FACULTY_STORAGE_PATH)
            .document(FacultyPosts.PROFESSOR.values)
            .collection("members_list")


        val refAssistantProfessor = db.collection(FACULTY_STORAGE_PATH)
            .document(FacultyPosts.ASSISTANT_PROFESSOR.values)
            .collection("members_list")

        val refAssociateProfessor = db.collection(FACULTY_STORAGE_PATH)
            .document(FacultyPosts.ASSOCIATE_PROFESSOR.values)
            .collection("members_list")


        val refLecturer = db.collection(FACULTY_STORAGE_PATH)
            .document(FacultyPosts.LECTURER.values)
            .collection("members_list")

        val refHonoraryProfessor = db.collection(FACULTY_STORAGE_PATH)
            .document(FacultyPosts.HONORARY_PROFESSOR.values)
            .collection("members_list")


        refHonoraryProfessor.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getAllFacultyList: ${error.message}")
                    error.printStackTrace()
                    return@EventListener
                }

                value?.toFacultyDataList()?.let { facultyDataList.addAll(it) }
                facultyDataList.let {
                    facultyDataListLiveData.value = it
                }
            }
        )

        refAssistantProfessor.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getAllFacultyList: ${error.message}")
                    error.printStackTrace()
                    return@EventListener
                }

                value?.toFacultyDataList()?.let { facultyDataList.addAll(it) }
                facultyDataList?.let {
                    facultyDataListLiveData.value = it
                }
            }
        )

        refLecturer.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getAllFacultyList: ${error.message}")
                    error.printStackTrace()
                    return@EventListener
                }

                value?.toFacultyDataList()?.let { facultyDataList.addAll(it) }
                facultyDataList?.let {
                    facultyDataListLiveData.value = it
                }
            }
        )

        refAssociateProfessor.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getAllFacultyList: ${error.message}")
                    error.printStackTrace()
                    return@EventListener
                }
                value?.toFacultyDataList()?.let { facultyDataList.addAll(it) }

                facultyDataList.let {
                    facultyDataListLiveData.value = it
                }
            }
        )

        refChairman.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getAllFacultyList: ${error.message}")
                    error.printStackTrace()
                    return@EventListener
                }

                value?.toFacultyDataList()?.let { facultyDataList.addAll(it) }

                facultyDataList.let {
                    facultyDataListLiveData.value = it
                }

            }
        )

        refProfessor.addSnapshotListener(
            EventListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "getAllFacultyList: ${error.message}")
                    error.printStackTrace()
                    return@EventListener
                }

                value?.toFacultyDataList()?.let { facultyDataList.addAll(it) }

                facultyDataList.let {
                    facultyDataListLiveData.value = it
                }

            }
        )

        return facultyDataListLiveData
    }


    companion object {
        private const val TAG = "FacultyRepository"
    }
}