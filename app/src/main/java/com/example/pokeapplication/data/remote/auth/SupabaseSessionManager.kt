package com.example.pokeapplication.data.remote.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
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

    suspend fun <T> withSession(request: suspend (userId: String) -> T): T {
        val userId = getUserId()

        val usedToken = checkNotNull(supabase.auth.currentAccessTokenOrNull()) {
            "Сессия пользователя недоступна"
        }

        return try {
            request(userId)
        } catch (exception: RestException) {
            val isAuthorizationError = exception is UnauthorizedRestException || exception.statusCode == 401

            if (!isAuthorizationError) {
                throw exception
            }

            mutex.withLock {
                val auth = supabase.auth

                checkNotNull(auth.currentSessionOrNull()) {
                    "Не удалось восстановить сессию пользователя"
                }

                if (auth.currentAccessTokenOrNull() == usedToken) {
                    auth.refreshCurrentSession()
                }
            }

            request(userId)
        }
    }
}