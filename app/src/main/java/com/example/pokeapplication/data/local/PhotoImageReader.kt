package com.example.pokeapplication.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoImageReader @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun readJpeg(sourceUri: String): ByteArray =
        withContext(Dispatchers.IO) {
            val source = ImageDecoder.createSource(context.contentResolver, sourceUri.toUri())

            val bitmap = ImageDecoder.decodeBitmap(source) {
                    decoder, info, _ ->

                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE

                val largestSide = maxOf(info.size.width, info.size.height)

                if (largestSide > MAX_SIDE) {
                    val scale = MAX_SIDE.toDouble() / largestSide

                    decoder.setTargetSize(
                        (info.size.width * scale).toInt().coerceAtLeast(1),
                        (info.size.height * scale).toInt().coerceAtLeast(1)
                    )
                }
            }

            try {
                ByteArrayOutputStream().use { output ->
                    check(
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            JPEG_QUALITY,
                            output
                        )
                    ) {
                        "Не удалось подготовить фотографию"
                    }

                    output.toByteArray()
                }
            } finally {
                bitmap.recycle()
            }
        }

    private companion object {
        const val MAX_SIDE = 1600
        const val JPEG_QUALITY = 85
    }
}