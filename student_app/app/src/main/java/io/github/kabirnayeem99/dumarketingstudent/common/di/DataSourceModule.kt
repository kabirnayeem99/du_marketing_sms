package io.github.kabirnayeem99.dumarketingstudent.common.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.dumarketingstudent.data.dataSources.AboutRemoteDataSource

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {
    @Provides
    fun provideAboutRemoteDataSource(db: FirebaseFirestore): AboutRemoteDataSource {
        return AboutRemoteDataSource(db)
    }
}