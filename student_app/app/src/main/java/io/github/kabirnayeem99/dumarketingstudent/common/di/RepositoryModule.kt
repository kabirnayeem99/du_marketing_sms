package io.github.kabirnayeem99.dumarketingstudent.common.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.dumarketingstudent.data.dataSources.AboutRemoteDataSource
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.*
import io.github.kabirnayeem99.dumarketingstudent.domain.repository.AboutRepository
import io.github.kabirnayeem99.dumarketingstudent.domain.repository.EbookRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAboutRepository(dataSource: AboutRemoteDataSource): AboutRepository {
        return DefaultAboutRepository(dataSource)
    }

    @Provides
    fun provideEbookRepository(db: FirebaseFirestore): EbookRepository {
        return DefaultEbookRepository(db)
    }

    @Provides
    fun provideFacultyRepository(db: FirebaseFirestore): FacultyRepository {
        return FacultyRepository(db)
    }

    @Provides
    fun provideGalleryRepository(db: FirebaseFirestore): GalleryRepository {
        return GalleryRepository(db)
    }

    @Provides
    fun provideNoticeRepository(db: FirebaseFirestore): NoticeRepository {
        return NoticeRepository(db)
    }
}