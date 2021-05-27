package io.github.kabirnayeem99.dumarketingstudent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import io.github.kabirnayeem99.dumarketingstudent.data.vo.EbookData
import io.github.kabirnayeem99.dumarketingstudent.data.vo.EbookData.Companion.toEbookDataList
import io.github.kabirnayeem99.dumarketingstudent.util.Constants.EBOOK_DB_REF
import io.github.kabirnayeem99.dumarketingstudent.util.Resource
import javax.inject.Inject

class EbookRepository @Inject constructor(var db: FirebaseFirestore) {

    fun getEbooks(): LiveData<Resource<List<EbookData>>> {
        val ebookLiveData = MutableLiveData<Resource<List<EbookData>>>()
        db.collection(EBOOK_DB_REF).addSnapshotListener(
            EventListener { value, e ->
                if (e != null) {

                    ebookLiveData.value =
                        Resource.Error(e.message ?: "Could not fetch from server.")

                    return@EventListener
                }

                value?.toEbookDataList().also { ebookDataList ->
                    ebookDataList?.let {
                        ebookLiveData.value = Resource.Success(it)
                    }
                }


            }
        )
        return ebookLiveData
    }

}