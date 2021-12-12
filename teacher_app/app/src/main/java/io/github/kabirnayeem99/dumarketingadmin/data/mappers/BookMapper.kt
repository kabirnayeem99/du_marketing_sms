package io.github.kabirnayeem99.dumarketingadmin.data.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.github.kabirnayeem99.dumarketingadmin.data.dto.GoogleBooksDto.RecommendedBookDto
import io.github.kabirnayeem99.dumarketingadmin.domain.data.EbookData
import java.util.*

object BookMapper {

    fun RecommendedBookDto.toBookData(): EbookData {
        val downloadUrl = canonicalVolumeLink
        val name = title
        val thumbnailUrl = imageLinks.thumbnail
        val authorName = authors.toString()
        return EbookData("$name-${Date()}", name, downloadUrl, thumbnailUrl, authorName)
    }

    fun DocumentSnapshot.toBookData(): EbookData {
        val name = this["title"].toString()
        val downloadUrl = this["pdfUrl"].toString()
        val thumbnailUrl = this["thumbnailUrl"].toString()
        val authorName = this["authorName"].toString()
        val id = this["key"].toString()
        return EbookData(id, downloadUrl, name, thumbnailUrl, authorName)
    }

    fun QuerySnapshot.toBookDataList(): List<EbookData> {
        val ebookDataList = mutableListOf<EbookData>()

        for (docSnapshot in this) {
            ebookDataList.add(docSnapshot.toBookData())
        }
        return ebookDataList
    }
}