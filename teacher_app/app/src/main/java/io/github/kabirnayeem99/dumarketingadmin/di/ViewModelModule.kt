package io.github.kabirnayeem99.dumarketingadmin.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.EbookDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.FacultyDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.GalleryDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.RoutineDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.*
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideEbookDataSource(db: FirebaseFirestore, store: FirebaseStorage): EbookDataSource {
        return EbookDataSource(db, store)
    }

    @Provides
    fun provideIoScope(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun provideEbookRepository(dataSource: EbookDataSource): EbookRepository {
        return DefaultEbookRepository(dataSource)
    }

    @Provides
    fun provideFacultyDataSource(db: FirebaseFirestore, store: FirebaseStorage): FacultyDataSource {
        return FacultyDataSource(db, store)
    }

    @Provides
    fun provideFacultyRepository(dataSource: FacultyDataSource): FacultyRepository {
        return DefaultFacultyRepository(dataSource)
    }

    @Provides
    fun provideGalleryDataSource(db: FirebaseFirestore, store: FirebaseStorage): GalleryDataSource {
        return GalleryDataSource(db, store)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun provideGalleryRepository(dataSource: GalleryDataSource): GalleryRepository {
        return DefaultGalleryRepository(dataSource)
    }

    @Provides
    fun provideInformationRepository(db: FirebaseFirestore) = InformationRepository(db)

    @Provides
    fun provideNoticeRepository(db: FirebaseFirestore, store: FirebaseStorage): NoticeRepository {
        return NoticeRepository(db, store)
    }

    @Provides
    fun provideAuthenticationRepository(auth: FirebaseAuth): AuthenticationRepository {
        return DefaultAuthenticationRepository(auth)
    }

    @Provides
    fun provideRoutineDataSource(db: FirebaseFirestore): RoutineDataSource {
        return RoutineDataSource(db)
    }

    @Provides
    fun provideRoutineRepository(datasource: RoutineDataSource): RoutineRepository {
        return DefaultRoutineRepository(datasource)
    }
}