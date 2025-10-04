/*
 * Service module for dependency injection
 */

package com.score24seven.di

import android.content.Context
import com.score24seven.service.NotificationService
import com.score24seven.util.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    // NotificationService is already provided by @Inject constructor
    // No additional provider needed

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager = PreferencesManager(context)
}