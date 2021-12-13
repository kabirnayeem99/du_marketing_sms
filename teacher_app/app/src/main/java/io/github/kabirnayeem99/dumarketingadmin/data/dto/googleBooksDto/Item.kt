package io.github.kabirnayeem99.dumarketingadmin.data.dto.googleBooksDto

data class Item(
    val accessInfo: AccessInfoDto,
    val etag: String,
    val id: String,
    val kind: String,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo,
    val selfLink: String,
    val volumeInfo: RecommendedBookDto
)