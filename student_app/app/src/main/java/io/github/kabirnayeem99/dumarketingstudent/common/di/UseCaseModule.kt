package io.github.kabirnayeem99.dumarketingstudent.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.DefaultEbookRepository
import io.github.kabirnayeem99.dumarketingstudent.domain.repository.AboutRepository
import io.github.kabirnayeem99.dumarketingstudent.domain.useCases.GetAboutDataUseCase
import io.github.kabirnayeem99.dumarketingstudent.domain.useCases.GetEbooksUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetEbookUseCase(repo: DefaultEbookRepository): GetEbooksUseCase {
        return GetEbooksUseCase(repo)
    }

    @Provides
    fun provideGetAboutDataUseCase(repo: AboutRepository): GetAboutDataUseCase {
        return GetAboutDataUseCase(repo)
    }
}