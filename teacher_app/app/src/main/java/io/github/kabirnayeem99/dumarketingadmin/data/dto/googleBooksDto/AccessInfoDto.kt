package io.github.kabirnayeem99.dumarketingadmin.data.dto.googleBooksDto

data class AccessInfoDto(
    val accessViewStatus: String,
    val country: String,
    val embeddable: Boolean,
    val pdf: Pdf,
    val publicDomain: Boolean,
    val quoteSharingAllowed: Boolean,
    val textToSpeechPermission: String,
    val viewability: String,
    val webReaderLink: String
)