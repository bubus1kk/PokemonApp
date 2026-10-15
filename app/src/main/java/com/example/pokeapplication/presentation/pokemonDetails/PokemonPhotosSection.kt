package com.example.pokeapplication.presentation.pokemondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pokeapplication.R
import com.example.pokeapplication.domain.model.PokemonPhoto

fun LazyListScope.pokemonPhotosSection(
    uiState: PokemonDetailsUiState,
    onRetry: () -> Unit,
    onChoosePhoto: (() -> Unit)?,
    onTakePhoto: (() -> Unit)?,
    onDeletePhoto: ((String) -> Unit)?,
    actionsEnabled: Boolean
) {
    item(key = "photos_header") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(R.string.photos_title), style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onRetry, enabled = actionsEnabled) {
                    Text(stringResource(R.string.photos_refresh), color = MaterialTheme.colorScheme.onSurface)
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PhotoSourceButton(
                    stringResource(R.string.photos_camera), R.drawable.ic_camera,
                    MaterialTheme.colorScheme.primaryContainer, Modifier.weight(1f),
                    enabled = actionsEnabled && !uiState.photosNeedReload && onTakePhoto != null,
                    onClick = { onTakePhoto?.invoke() }
                )
                PhotoSourceButton(
                    stringResource(R.string.photos_gallery), R.drawable.ic_gallery,
                    MaterialTheme.colorScheme.secondaryContainer, Modifier.weight(1f),
                    enabled = actionsEnabled && !uiState.photosNeedReload && onChoosePhoto != null,
                    onClick = { onChoosePhoto?.invoke() }
                )
            }
        }
    }
    if (uiState.isPhotosLoading) {
        item(key = "photos_loading") {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                val label = stringResource(R.string.photos_loading)
                CircularProgressIndicator(Modifier.size(20.dp).semantics { contentDescription = label },
                    strokeWidth = 2.dp)
                Text(label, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    if (uiState.photosErrorMessage != null) {
        item(key = "photos_error") {
            Surface(shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surface) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(uiState.photosErrorMessage, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    TextButton(onClick = onRetry, enabled = actionsEnabled) {
                        Text(stringResource(R.string.photos_refresh), color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
    if (uiState.photos.isEmpty() && !uiState.isPhotosLoading && uiState.photosErrorMessage == null) {
        item(key = "photos_empty") {
            Surface(shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surface) {
                Column(Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                        Icon(painterResource(R.drawable.ic_gallery), null, Modifier.padding(16.dp).size(28.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(stringResource(R.string.photos_empty), style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }
    }
    // Rows belong to the existing LazyColumn; no nested vertically scrolling grid.
    items(uiState.photos.chunked(2), key = { "photos_row_${it.first().id}" }) { row ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            row.forEach { photo ->
                PhotoCard(photo, Modifier.weight(1f),
                    isDeleting = uiState.deletingPhotoId == photo.id,
                    deleteEnabled = actionsEnabled && onDeletePhoto != null,
                    onDelete = { onDeletePhoto?.invoke(photo.id) })
            }
            if (row.size == 1) Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun PhotoSourceButton(
    label: String,
    icon: Int,
    color: Color,
    modifier: Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(onClick = onClick, enabled = enabled, modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = if (enabled) color else MaterialTheme.colorScheme.surfaceVariant) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(painterResource(icon), null, Modifier.size(22.dp))
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun PhotoCard(
    photo: PokemonPhoto,
    modifier: Modifier,
    isDeleting: Boolean,
    deleteEnabled: Boolean,
    onDelete: () -> Unit
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface) {
        Column(Modifier.padding(8.dp)) {
            AlbumPhotoImage(photo.imageUrl, stringResource(R.string.photos_image),
                Modifier.fillMaxWidth().aspectRatio(1f))
            TextButton(onClick = onDelete, enabled = deleteEnabled,
                modifier = Modifier.align(Alignment.End)) {
                if (isDeleting) {
                    val label = stringResource(R.string.photos_deleting)
                    CircularProgressIndicator(Modifier.size(18.dp).semantics { contentDescription = label },
                        strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.photos_delete), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
internal fun AlbumPhotoImage(
    model: String,
    description: String,
    modifier: Modifier,
    onReady: (Boolean) -> Unit = {}
) {
    var loading by remember(model) { mutableStateOf(true) }
    var failed by remember(model) { mutableStateOf(false) }
    Box(modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center) {
        AsyncImage(model = model, contentDescription = description,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit,
            onLoading = { loading = true; failed = false; onReady(false) },
            onSuccess = { loading = false; failed = false; onReady(true) },
            onError = { loading = false; failed = true; onReady(false) })
        if (loading) CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
        if (failed) Text(stringResource(R.string.photos_image_error), Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}
