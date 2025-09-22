/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Score24SevenApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize any app-level services here
        // - Analytics
        // - Crash reporting
        // - Push notifications
        // - WebSocket service
    }
}