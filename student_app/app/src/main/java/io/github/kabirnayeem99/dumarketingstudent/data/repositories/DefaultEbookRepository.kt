package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.common.util.Constants.EBOOK_DB_REF
import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.EbookData
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.EbookData.Companion.toEbookDataList
import io.github.kabirnayeem99.dumarketingstudent.domain.repository.EbookRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DefaultEbookRepository @Inject constructor(var db: FirebaseFirestore) : EbookRepository {

    override fun getEbooks(): Flow<Resource<List<EbookData>>> =
        callbackFlow<Resource<List<EbookData>>> {
            db.collection(EBOOK_DB_REF).addSnapshotListener { value, error ->
                if (error != null)
                    trySend(
                        Resource.Error(
                            error.localizedMessage ?: "Could not fetch from server."
                        )
                    )
                value?.toEbookDataList()?.also { ebookList ->
                    trySend(Resource.Success(ebookList))
                }
            }

            awaitClose { cancel() }
        }

}