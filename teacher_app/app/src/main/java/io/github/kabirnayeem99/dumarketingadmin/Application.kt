package io.github.kabirnayeem99.dumarketingadmin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DuMarketingAdminApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpLogging()
    }

    private fun setUpLogging() {
        Timber.plant(Timber.DebugTree())
    }
}