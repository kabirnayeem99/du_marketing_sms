package io.github.kabirnayeem99.dumarketingadmin.data.dto

import androidx.annotation.Keep

@Keep
data class EbookDto(
    var title: String,
    var pdfUrl: String,
    var key: String? = null
)
