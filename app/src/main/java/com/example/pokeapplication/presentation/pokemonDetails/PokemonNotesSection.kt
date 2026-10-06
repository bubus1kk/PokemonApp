package com.example.pokeapplication.presentation.pokemondetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.pokeapplication.R
import com.example.pokeapplication.domain.model.PokemonNote

fun LazyListScope.pokemonNotesSection(
    notes: List<PokemonNote>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onAddNote: (() -> Unit)? = null,
    isSavingNote: Boolean = false,
    deletingNoteId: Int? = null,
    actionErrorMessage: String? = null,
    showNoteEditor: Boolean = false,
    onEditNote: ((Int) -> Unit)? = null,
    onDeleteNote: ((Int) -> Unit)? = null
) {
    val actionsEnabled = !isSavingNote && deletingNoteId == null && !showNoteEditor
    item(key = "notes_header") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.notes_title), style = MaterialTheme.typography.titleMedium)
            if (onAddNote != null) {
                TextButton(onClick = onAddNote, enabled = actionsEnabled) {
                    Text(stringResource(R.string.notes_add))
                }
            }
        }
    }
    if (isLoading) {
        item(key = "notes_loading") {
            val description = stringResource(R.string.notes_loading)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp).semantics { contentDescription = description },
                    strokeWidth = 2.dp
                )
                Text(description, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    if (errorMessage != null) {
        item(key = "notes_error") {
            NotesSurface {
                Text(errorMessage, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = onRetry, enabled = !isLoading) {
                    Text(stringResource(R.string.home_retry), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
    if (actionErrorMessage != null) {
        item(key = "notes_action_error") {
            Text(actionErrorMessage, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error)
        }
    }
    if (notes.isEmpty() && !isLoading && errorMessage == null) {
        item(key = "notes_empty") {
            NotesSurface {
                Text(stringResource(R.string.notes_empty), style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    items(notes, key = { "note_${it.id}" }, contentType = { "note" }) { note ->
        NotesSurface {
            Text(note.noteText, style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface)
            if (onEditNote != null || onDeleteNote != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onEditNote != null) {
                        TextButton(onClick = { onEditNote(note.id) }, enabled = actionsEnabled) {
                            Text(stringResource(R.string.notes_edit))
                        }
                    }
                    if (onDeleteNote != null) {
                        TextButton(onClick = { onDeleteNote(note.id) }, enabled = actionsEnabled) {
                            if (deletingNoteId == note.id) {
                                val description = stringResource(R.string.notes_deleting)
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp).semantics {
                                        contentDescription = description
                                    },
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(stringResource(R.string.notes_delete),
                                    color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotesSurface(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            content()
        }
    }
}
