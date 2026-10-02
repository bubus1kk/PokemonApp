package com.example.pokeapplication.presentation.pokemonlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.pokeapplication.R
import com.example.pokeapplication.domain.model.PokemonSortOrder
import com.example.pokeapplication.ui.theme.PokedexShadow
import java.util.Locale

// Options remain available even when no Pokémon matches the selected filters.
private val pokemonTypes = listOf(
    "grass", "fire", "water", "electric", "normal", "poison", "bug", "flying",
    "ground", "rock", "fighting", "psychic", "ice", "ghost", "dragon", "dark", "steel", "fairy"
)

@Composable
fun PokemonFiltersPanel(
    typeName: String?,
    sortOrder: PokemonSortOrder,
    onTypeChanged: (String?) -> Unit,
    onSortOrderChanged: (PokemonSortOrder) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasFilters = typeName != null || sortOrder != PokemonSortOrder.ID_ASC
    val shape = RoundedCornerShape(22.dp)
    Surface(
        modifier = modifier.fillMaxWidth().shadow(4.dp, shape,
            ambientColor = PokedexShadow.copy(alpha = .04f),
            spotColor = PokedexShadow.copy(alpha = .08f)),
        shape = shape,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.filters_title), modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onReset, enabled = hasFilters) {
                    Text(stringResource(R.string.filters_reset), style = MaterialTheme.typography.labelMedium,
                        color = if (hasFilters) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(stringResource(R.string.filters_type), style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item(key = "all") {
                    FilterOption(stringResource(R.string.filters_all), typeName == null,
                        onClick = { onTypeChanged(null) })
                }
                items(pokemonTypes, key = { it }) { type ->
                    FilterOption(type.replaceFirstChar { it.titlecase(Locale.ROOT) }, typeName == type,
                        onClick = { onTypeChanged(if (typeName == type) null else type) })
                }
            }
            Text(stringResource(R.string.filters_sort), style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PokemonSortOrder.entries.forEach { order ->
                    val label = stringResource(when (order) {
                        PokemonSortOrder.ID_ASC -> R.string.filters_number_asc
                        PokemonSortOrder.ID_DESC -> R.string.filters_number_desc
                        PokemonSortOrder.NAME_ASC -> R.string.filters_name_asc
                        PokemonSortOrder.NAME_DESC -> R.string.filters_name_desc
                    })
                    FilterOption(label, sortOrder == order, onClick = { onSortOrderChanged(order) },
                        selectedColor = MaterialTheme.colorScheme.secondaryContainer)
                }
            }
        }
    }
}

@Composable
private fun FilterOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.semantics { selected = isSelected },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) selectedColor else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(label, modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge)
    }
}
