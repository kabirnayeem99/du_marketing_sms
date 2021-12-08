package io.github.kabirnayeem99.dumarketingadmin.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.dumarketingadmin.data.dataSources.EbookDataSource
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.*
import io.github.kabirnayeem99.dumarketingadmin.domain.repositories.EbookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideEbookDataSource(db: FirebaseFirestore, store: FirebaseStorage): EbookDataSource {
        return EbookDataSource(db, store)
    }

    @Provides
    fun provideIoScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun provideEbookRepository(dataSource: EbookDataSource): EbookRepository {
        return DefaultEbookRepository(dataSource)
    }

    @Provides
    fun provideFacultyRepository(db: FirebaseFirestore, store: FirebaseStorage): FacultyRepository {
        return FacultyRepository(db, store)
    }

    @Provides
    fun provideGalleryRepository(db: FirebaseFirestore, store: FirebaseStorage): GalleryRepository {
        return GalleryRepository(db, store)
    }

    @Provides
    fun provideInformationRepository(db: FirebaseFirestore) = InformationRepository(db)

    @Provides
    fun provideNoticeRepository(db: FirebaseFirestore, store: FirebaseStorage): NoticeRepository {
        return NoticeRepository(db, store)
    }

    @Provides
    fun provideRoutineRepository(db: FirebaseFirestore) = RoutineRepository(db)
}