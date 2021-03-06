package io.github.kabirnayeem99.dumarketingadmin.data.dataSources

import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData.Companion.toInformationData
import io.github.kabirnayeem99.dumarketingadmin.common.util.Constants
import io.github.kabirnayeem99.dumarketingadmin.common.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InfoDataSource @Inject constructor(private var db: FirebaseFirestore) {
    suspend fun saveInformationData(informationData: InformationData): Resource<String> {
        return try {
            val key: String = Constants.ABOUT_DB_REF
            db.collection(Constants.ABOUT_DB_REF).document(key).set(informationData).await()
            Resource.Success(informationData.toString())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load the information")
        }
    }

    suspend fun getInformationData(): Resource<InformationData> {
        val result: Resource<InformationData> = try {
            val key: String = Constants.ABOUT_DB_REF

            val informationData =
                db.collection(Constants.ABOUT_DB_REF).document(key).get().await()
                    .toInformationData()
            Resource.Success(informationData)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load information data.")
        }

        return result
    }

}