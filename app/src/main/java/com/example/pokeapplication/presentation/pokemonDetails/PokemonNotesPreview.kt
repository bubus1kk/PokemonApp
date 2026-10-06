package com.example.pokeapplication.presentation.pokemondetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokeapplication.domain.model.PokemonNote
import com.example.pokeapplication.ui.theme.PokeApplicationTheme

@Composable
private fun NotesPreviewContent(
    notes: List<PokemonNote> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    PokeApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LazyColumn(
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                pokemonNotesSection(notes, isLoading, errorMessage, onRetry = {})
            }
        }
    }
}

@Preview(name = "Notes", widthDp = 390, heightDp = 480)
@Preview(name = "Compact notes", widthDp = 320, heightDp = 480, fontScale = 1.3f)
@Composable
private fun NotesListPreview() {
    NotesPreviewContent(notes = listOf(
        PokemonNote(2, 1, "Useful against Water-type Pokémon."),
        PokemonNote(1, 1, "My first starter Pokémon. I want to keep track of its abilities and evolution here.")
    ))
}

@Preview(name = "Empty notes", widthDp = 390, heightDp = 240)
@Composable
private fun EmptyNotesPreview() { NotesPreviewContent() }

@Preview(name = "Loading notes", widthDp = 390, heightDp = 240)
@Composable
private fun LoadingNotesPreview() { NotesPreviewContent(isLoading = true) }

@Preview(name = "Notes error", widthDp = 390, heightDp = 280)
@Composable
private fun NotesErrorPreview() { NotesPreviewContent(errorMessage = "Не удалось загрузить заметки") }
