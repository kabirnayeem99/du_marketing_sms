package io.github.kabirnayeem99.dumarketingstudent.presentation.about

import io.github.kabirnayeem99.dumarketingstudent.domain.entity.AboutData

data class AboutUiState(
    val isLoading: Boolean = false,
    val message: String = "",
    val about: AboutData = AboutData(),
)
