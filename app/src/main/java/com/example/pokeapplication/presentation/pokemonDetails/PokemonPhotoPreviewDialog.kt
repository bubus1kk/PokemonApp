package com.example.pokeapplication.presentation.pokemondetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.pokeapplication.R

@Composable
fun PokemonPhotoPreviewDialog(
    sourceUri: String,
    isUploading: Boolean,
    canUpload: Boolean,
    onUpload: () -> Unit,
    onDismiss: () -> Unit
) {
    var imageReady by remember(sourceUri) { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = { if (!isUploading) onDismiss() },
        properties = DialogProperties(dismissOnBackPress = !isUploading,
            dismissOnClickOutside = !isUploading),
        shape = RoundedCornerShape(28.dp), containerColor = MaterialTheme.colorScheme.surface,
        title = { Text(stringResource(R.string.photos_preview_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AlbumPhotoImage(sourceUri, stringResource(R.string.photos_preview_image),
                    Modifier.fillMaxWidth().aspectRatio(1f), onReady = { imageReady = it })
                Text(stringResource(if (isUploading) R.string.photos_uploading else R.string.photos_preview_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = {
            TextButton(onClick = onUpload, enabled = canUpload && imageReady && !isUploading) {
                if (isUploading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text(stringResource(R.string.photos_upload), color = MaterialTheme.colorScheme.onSurface)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isUploading) {
                Text(stringResource(R.string.notes_cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
