package io.github.kabirnayeem99.dumarketingadmin.data.dto.GoogleBooksDto

data class Item(
    val accessInfo: AccessInfo,
    val etag: String,
    val id: String,
    val kind: String,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo,
    val selfLink: String,
    val volumeInfo: RecommendedBookDto
)