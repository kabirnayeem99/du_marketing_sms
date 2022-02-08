package io.github.kabirnayeem99.dumarketingstudent.common.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.dumarketingstudent.data.dataSources.AboutRemoteDataSource
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideAboutRemoteDataSource(db: FirebaseFirestore): AboutRemoteDataSource {
        return AboutRemoteDataSource(db)
    }
}