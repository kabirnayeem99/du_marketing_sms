package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.FacultyData
import io.github.kabirnayeem99.dumarketingstudent.util.enums.FacultyPosts

class FacultyRepository {

    private val facultyDataListLiveData = MutableLiveData<List<FacultyData>>()

    fun getAllFacultyList(): LiveData<List<FacultyData>> {
        val facultyDataList = listOf(
            FacultyData(
                "Rana Hossain",
                "0182945933",
                "himel@du.ac.bd",
                FacultyPosts.ASSISTANT_PROFESSOR.values,
                "https://media-eng.dhakatribune.com/uploads/2020/11/unnamed-1606052632291.jpg"
            ),
            FacultyData(
                "Himel Mia",
                "0182945933",
                "Tama@du.ac.bd",
                FacultyPosts.ASSISTANT_PROFESSOR.values,
                "https://media-eng.dhakatribune.com/uploads/2020/11/unnamed-1606052632291.jpg"
            ),
            FacultyData(
                "Muktar Hossain",
                "0182945933",
                "himel@du.ac.bd",
                post = FacultyPosts.ASSISTANT_PROFESSOR.values,
                "https://hr.bup.edu.bd/upload/picture/11526.JPG"
            ),
            FacultyData(
                "Himel Hossain",
                "0182945933",
                "himel@du.ac.bd",
                FacultyPosts.PROFESSOR.values,
                "https://media-eng.dhakatribune.com/uploads/2020/11/unnamed-1606052632291.jpg"
            ),
            FacultyData(
                "Akter Hossain",
                "0182945933",
                "himel@du.ac.bd",
                FacultyPosts.CHAIRMAN.values,
                "https://hr.bup.edu.bd/upload/picture/11526.JPG"
            ),
        )

        facultyDataListLiveData.value = facultyDataList

        return facultyDataListLiveData
    }
}