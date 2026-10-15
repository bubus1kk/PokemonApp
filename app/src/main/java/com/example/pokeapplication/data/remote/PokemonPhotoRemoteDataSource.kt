package com.example.pokeapplication.data.remote

import com.example.pokeapplication.data.remote.auth.SupabaseSessionManager
import com.example.pokeapplication.data.remote.dto.NewPokemonPhotoDto
import com.example.pokeapplication.data.remote.dto.PokemonPhotoDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class PokemonPhotoRemoteDataSource @Inject constructor(
    private val supabase: SupabaseClient,
    private val sessionManager: SupabaseSessionManager
) {
    suspend fun getPhotos(pokemonId: Int): List<PokemonPhotoDto> =
        sessionManager.withSession { userId ->
            supabase.from("pokemon_photos")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("pokemon_id", pokemonId)
                    }

                    order("created_at", Order.DESCENDING)
                }
                .decodeList<PokemonPhotoDto>()
        }

    suspend fun getPhotoUrl(storagePath: String):
            String = sessionManager.withSession {
            supabase.storage
                .from("pokeApp-photos")
                .createSignedUrl(
                    path = storagePath,
                    expiresIn = 1.hours
                )
        }

    suspend fun uploadFile(storagePath: String, jpegBytes: ByteArray) {
        require(jpegBytes.isNotEmpty()) {
            "Фотография не может быть пустой"
        }

        sessionManager.withSession {
            supabase.storage
                .from("pokeApp-photos")
                .upload(storagePath, jpegBytes) {
                    contentType = ContentType.Image.JPEG
                    upsert = false
                }
        }
    }

    suspend fun insertPhoto(photo: NewPokemonPhotoDto): PokemonPhotoDto =
        sessionManager.withSession {
            supabase.from("pokemon_photos")
                .insert(photo) {
                    select()
                }
                .decodeSingle<PokemonPhotoDto>()
        }

    suspend fun getPhotoById(photoId: String): PokemonPhotoDto? =
        sessionManager.withSession { userId ->
            supabase.from("pokemon_photos")
                .select {
                    filter {
                        eq("id", photoId)
                        eq("user_id", userId)
                    }
                }
                .decodeSingleOrNull<PokemonPhotoDto>()
        }

    suspend fun deleteFile(storagePath: String) {
        sessionManager.withSession {
            supabase.storage
                .from("pokeApp-photos")
                .delete(storagePath)
        }
    }

    suspend fun deletePhotoRecord(photoId: String) {
        sessionManager.withSession { userId ->
            supabase.from("pokemon_photos")
                .delete {
                    filter {
                        eq("id", photoId)
                        eq("user_id", userId)
                    }
                }
        }
    }
}