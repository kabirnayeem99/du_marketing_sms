package io.github.kabirnayeem99.dumarketingstudent.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.dumarketingstudent.data.repositories.*

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideAboutRepository(db: FirebaseFirestore): AboutRepository {
        return AboutRepository(db)
    }

    @Provides
    fun provideEbookRepository(db: FirebaseFirestore): EbookRepository {
        return EbookRepository(db)
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