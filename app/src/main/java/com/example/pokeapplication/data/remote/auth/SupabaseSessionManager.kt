package com.example.pokeapplication.data.remote.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class SupabaseSessionManager @Inject constructor(private val supabase: SupabaseClient) {
    private val mutex = Mutex()

    suspend fun getUserId(): String = mutex.withLock {
        val auth = supabase.auth

        auth.awaitInitialization()

        if (auth.currentSessionOrNull() == null) {
            check(
                auth.sessionStatus.value is SessionStatus.NotAuthenticated
            ) {
                "Не удалось восстановить сессию пользователя"
            }

            auth.signInAnonymously()
        }

        checkNotNull(auth.currentUserOrNull()?.id) {
            "Не удалось получить ID пользователя"
        }
    }
}