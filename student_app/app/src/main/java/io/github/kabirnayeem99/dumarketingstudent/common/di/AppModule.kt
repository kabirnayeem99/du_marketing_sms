package io.github.kabirnayeem99.dumarketingstudent.common.di

import android.content.Context
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.presentation.adapters.RoutineDataAdapter
import io.github.kabirnayeem99.dumarketingstudent.common.util.Preferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext app: Context): Preferences {
        return Preferences(app)
    }


    @Provides
    @Singleton
    fun provideBottomSheetDialog(@ApplicationContext app: Context): BottomSheetDialog {
        return BottomSheetDialog(app.applicationContext, R.style.BottomSheetDialogTheme)
    }


    @Provides
    @Singleton
    fun provideRoutineAdapter(scale: ScaleAnimation): RoutineDataAdapter {
        return RoutineDataAdapter(scale)
    }

    @Provides
    fun provideScaleAnimation(): ScaleAnimation {
        val scale = ScaleAnimation(
            0F,
            1F,
            0F,
            1F,
            ScaleAnimation.RELATIVE_TO_PARENT,
            .3f,
            ScaleAnimation.RELATIVE_TO_PARENT,
            .3f
        )
        scale.duration = 220
        scale.interpolator = OvershootInterpolator()
        return scale
    }
}