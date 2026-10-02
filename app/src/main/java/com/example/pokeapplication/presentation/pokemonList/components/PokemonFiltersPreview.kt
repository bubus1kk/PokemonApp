package com.example.pokeapplication.presentation.pokemonlist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokeapplication.domain.model.PokemonSortOrder
import com.example.pokeapplication.ui.theme.PokeApplicationTheme

@Preview(name = "Filters", widthDp = 390, showBackground = true)
@Preview(name = "Compact filters", widthDp = 320, showBackground = true)
@Preview(name = "Large text filters", widthDp = 320, fontScale = 1.3f, showBackground = true)
@Composable
fun PokemonFiltersPreview() {
    var typeName by remember { mutableStateOf<String?>("grass") }
    var sortOrder by remember { mutableStateOf(PokemonSortOrder.ID_ASC) }
    PokeApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PokemonFiltersPanel(
                typeName = typeName,
                sortOrder = sortOrder,
                onTypeChanged = { typeName = it },
                onSortOrderChanged = { sortOrder = it },
                onReset = { typeName = null; sortOrder = PokemonSortOrder.ID_ASC },
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}
