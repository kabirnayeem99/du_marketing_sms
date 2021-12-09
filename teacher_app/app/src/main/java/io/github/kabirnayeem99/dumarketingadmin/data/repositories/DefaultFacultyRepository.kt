package io.github.kabirnayeem99.dumarketingadmin.data.repositories

import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.FacultyDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.model.FacultyData
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.FacultyRepository
import io.github.kabirnayeem99.dumarketingadmin.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultFacultyRepository @Inject constructor(
    private val dataSource: FacultyDataSource,
) : FacultyRepository {
    override suspend fun uploadImage(imageFile: ByteArray, imageName: String): Resource<String> {
        return dataSource.uploadImageToRemote(imageFile, imageName)
    }

    override suspend fun saveFacultyData(facultyData: FacultyData): Resource<String> {
        return dataSource.saveFacultyDataToRemote(facultyData)
    }

    override suspend fun deleteFacultyData(facultyData: FacultyData): Resource<String> {
        return dataSource.deleteFacultyDataFromRemote(facultyData)
    }

    override fun getFacultyList(): Flow<Resource<List<FacultyData>>> {
        return dataSource.getFacultyList()
    }

}