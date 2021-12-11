package io.github.kabirnayeem99.dumarketingadmin.data.mappers

import io.github.kabirnayeem99.dumarketingadmin.data.dto.RecommendedBookDto
import io.github.kabirnayeem99.dumarketingadmin.domain.data.BookOpenBook
import java.util.*

object RecommendedBookMapper {

    fun RecommendedBookDto.toBookOpenBook(): BookOpenBook {
        val downloadUrl = canonicalVolumeLink
        val name = title
        return BookOpenBook("$name-${Date()}", downloadUrl, name)
    }
}