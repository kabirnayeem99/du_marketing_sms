package io.github.kabirnayeem99.dumarketingstudent.domain.repository

import io.github.kabirnayeem99.dumarketingstudent.common.util.Resource
import io.github.kabirnayeem99.dumarketingstudent.domain.entity.AboutData
import kotlinx.coroutines.flow.Flow

interface AboutRepository {
    fun getAboutData(): Flow<Resource<AboutData>>
}