package com.example.pokeapplication.data.repository

import com.example.pokeapplication.data.local.PhotoImageReader
import com.example.pokeapplication.data.remote.PokemonPhotoRemoteDataSource
import com.example.pokeapplication.data.remote.auth.SupabaseSessionManager
import com.example.pokeapplication.data.remote.dto.NewPokemonPhotoDto
import com.example.pokeapplication.data.remote.dto.PokemonPhotoDto
import com.example.pokeapplication.domain.model.PokemonPhoto
import com.example.pokeapplication.domain.repository.PokemonPhotoRepository
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class PokemonPhotoRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonPhotoRemoteDataSource,
    private val imageReader: PhotoImageReader,
    private val sessionManager: SupabaseSessionManager
) : PokemonPhotoRepository {

    override suspend fun getPhotos(pokemonId: Int): List<PokemonPhoto> {
        require(pokemonId > 0) {
            "ID покемона должен быть положительным"
        }

        return remoteDataSource.getPhotos(pokemonId).map { photo ->
            toDomain(photo)
        }
    }

    override suspend fun uploadPhoto(pokemonId: Int, sourceUri: String): PokemonPhoto {
        require(pokemonId > 0) {
            "ID покемона должен быть положительным"
        }
        require(sourceUri.isNotBlank()) {
            "Необходимо выбрать фотографию"
        }

        val jpegBytes = imageReader.readJpeg(sourceUri)
        val userId = sessionManager.getUserId()
        val photoId = UUID.randomUUID().toString()
        val storagePath = "$userId/$pokemonId/$photoId.jpg"

        val newPhoto = NewPokemonPhotoDto(
            id = photoId,
            userId = userId,
            pokemonId = pokemonId,
            storagePath = storagePath
        )

        remoteDataSource.uploadFile(storagePath, jpegBytes)

        val savedPhoto = insertPhotoOrRecover(newPhoto)

        return toDomain(savedPhoto)
    }

    override suspend fun deletePhoto(photoId: String) {
        require(photoId.isNotBlank()) {
            "ID фотографии не может быть пустым"
        }

        val photo = remoteDataSource.getPhotoById(photoId) ?: return

        remoteDataSource.deleteFile(photo.storagePath)
        remoteDataSource.deletePhotoRecord(photo.id)
    }

    private suspend fun toDomain(photo: PokemonPhotoDto): PokemonPhoto {
        val imageUrl = remoteDataSource.getPhotoUrl(
            photo.storagePath
        )

        return PokemonPhoto(
            id = photo.id,
            pokemonId = photo.pokemonId,
            imageUrl = imageUrl
        )
    }

    private suspend fun insertPhotoOrRecover(photo: NewPokemonPhotoDto): PokemonPhotoDto {
        try {
            return remoteDataSource.insertPhoto(photo)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            val existingPhoto = try {
                remoteDataSource.getPhotoById(photo.id)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (checkException: Exception) {
                exception.addSuppressed(checkException)
                throw exception
            }

            if (existingPhoto != null) {
                return existingPhoto
            }

            try {
                remoteDataSource.deleteFile(photo.storagePath)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (cleanupException: Exception) {
                exception.addSuppressed(cleanupException)
            }

            throw exception
        }
    }
}