package com.cotrip.mobile.data.network

import io.github.jan_tennert.supabase.createSupabaseClient
import io.github.jan_tennert.supabase.gotrue.GoTrue
import io.github.jan_tennert.supabase.postgrest.Postgrest
import io.ktor.client.engine.android.Android
import com.cotrip.mobile.BuildConfig
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    
    @Provides
    @Singleton
    fun provideSupabaseClient() = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        install(GoTrue)
    }
}
