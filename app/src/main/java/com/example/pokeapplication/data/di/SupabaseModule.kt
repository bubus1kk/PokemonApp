package com.example.pokeapplication.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://tajpcttiqkigozowxhpk.supabase.co",
            supabaseKey = "sb_publishable_2soqqziRR66T0MNI1wzv2A_gfXbbslc"
        ) {
            install(Auth)
            install(Storage)
            install(Postgrest)
        }
    }
}