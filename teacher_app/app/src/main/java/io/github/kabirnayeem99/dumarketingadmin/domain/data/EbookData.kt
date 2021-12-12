package io.github.kabirnayeem99.dumarketingadmin.domain.data

data class EbookData(
    var id: String,
    var name: String,
    var downloadUrl: String,
    var thumbnailUrl: String = "",
    var authorName: String = "Unknown",
)
