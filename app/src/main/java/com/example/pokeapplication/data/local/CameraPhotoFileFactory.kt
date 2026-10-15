package com.example.pokeapplication.data.local

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object CameraPhotoFileFactory {

    fun createUri(context: Context): Uri {
        val directory = File(context.cacheDir, "camera_photos")

        check(directory.isDirectory || directory.mkdirs()) {
            "Не удалось создать папку для фотографии"
        }

        val file = File.createTempFile("pokemon_photo_", ".jpg", directory)

        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }
}