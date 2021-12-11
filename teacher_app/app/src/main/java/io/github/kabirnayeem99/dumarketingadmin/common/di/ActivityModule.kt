package io.github.kabirnayeem99.dumarketingadmin.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {
    @Provides
    fun provideIoScope(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}