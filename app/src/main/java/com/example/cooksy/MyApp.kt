package com.example.cooksy



import android.app.Application
import com.example.cooksy.BuildConfig
import com.google.firebase.FirebaseApp
import timber.log.Timber

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    } else {
        // In production, you might plant a tree that logs to a crash reporting service.
        // For example, if using Firebase Crashlytics:
        // Timber.plant(CrashlyticsTree()) // You'd need to create this class
    }
}
}
