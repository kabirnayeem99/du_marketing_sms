package io.github.kabirnayeem99.dumarketingadmin.domain.repositories

import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface FacultyRepository {
    suspend fun uploadImage(imageFile: ByteArray, imageName: String): Resource<String>
    suspend fun saveFacultyData(facultyData: FacultyData): Resource<String>
    suspend fun deleteFacultyData(facultyData: FacultyData): Resource<String>
    fun getFacultyList(): Flow<Resource<List<FacultyData>>>
}