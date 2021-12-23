package io.github.kabirnayeem99.dumarketingstudent

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DuMarketingStudentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setUpLogging()
    }

    private fun setUpLogging() {
        Timber.plant(Timber.DebugTree())
    }
}