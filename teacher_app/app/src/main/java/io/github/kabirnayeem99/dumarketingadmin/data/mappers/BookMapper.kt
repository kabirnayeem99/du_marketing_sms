package io.github.kabirnayeem99.dumarketingadmin.data.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.github.kabirnayeem99.dumarketingadmin.common.ktx.toCommaSeparatedList
import io.github.kabirnayeem99.dumarketingadmin.data.dto.googleBooksDto.RecommendedBookDto
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import java.util.*

object BookMapper {

    fun RecommendedBookDto.toBookData(): EbookData {
        val downloadUrl = canonicalVolumeLink
        val name = title
        val thumbnailUrl = imageLinks.thumbnail
        val authorName =
            if (authors.isNullOrEmpty()) "" else (authors as ArrayList<String>).toCommaSeparatedList()
        val id = UUID.randomUUID().toString()
        return EbookData(id, name, downloadUrl, thumbnailUrl, authorName)
    }

    fun DocumentSnapshot.toBookData(): EbookData {
        val name = this["name"].toString()
        val downloadUrl = this["downloadUrl"].toString()
        val thumbnailUrl = this["thumbnailUrl"].toString()
        val authorName = this["authorName"].toString()
        val id = this.id
        return EbookData(id, name, downloadUrl, thumbnailUrl, authorName)
    }

    fun QuerySnapshot.toBookDataList(): List<EbookData> {
        val ebookDataList = mutableListOf<EbookData>()

        for (docSnapshot in this) {
            ebookDataList.add(docSnapshot.toBookData())
        }
        return ebookDataList
    }
}