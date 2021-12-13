package io.github.kabirnayeem99.dumarketingadmin.data.dto.googleBooksDto

data class GoogleBooksResponseDto(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)