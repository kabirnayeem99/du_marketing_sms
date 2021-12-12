package io.github.kabirnayeem99.dumarketingadmin.data.dto.GoogleBooksDto

data class GoogleBooksResponseDto(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)