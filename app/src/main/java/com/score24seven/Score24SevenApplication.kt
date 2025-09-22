/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven

import android.app.Application
import com.score24seven.util.PreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Score24SevenApplication : Application() {

    companion object {
        lateinit var instance: Score24SevenApplication
            private set

        val preferencesManager: PreferencesManager by lazy {
            PreferencesManager(instance)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize any app-level services here
        // - Analytics
        // - Crash reporting
        // - Push notifications
        // - WebSocket service
    }
}