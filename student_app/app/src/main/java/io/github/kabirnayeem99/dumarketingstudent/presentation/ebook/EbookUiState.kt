package io.github.kabirnayeem99.dumarketingstudent.presentation.ebook

import io.github.kabirnayeem99.dumarketingstudent.domain.entity.EbookData

data class EbookUiState(
    val isLoading: Boolean = false,
    val message: String = "",
    val ebookList: List<EbookData> = emptyList(),
)
