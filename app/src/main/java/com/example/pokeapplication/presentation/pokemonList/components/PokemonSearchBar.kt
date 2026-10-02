package com.example.pokeapplication.presentation.pokemonlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.pokeapplication.R
import com.example.pokeapplication.ui.theme.PokedexShadow

@Composable
fun PokemonSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {},
    filtersExpanded: Boolean = false,
    hasActiveFilters: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val searchHint = stringResource(R.string.home_search_hint)
    val filterLabel = stringResource(if (filtersExpanded) R.string.filters_close else R.string.home_filter)
    val shape = RoundedCornerShape(18.dp)

    Row(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.weight(1f).shadow(
                elevation = 5.dp, shape = shape,
                ambientColor = PokedexShadow.copy(alpha = 0.04f),
                spotColor = PokedexShadow.copy(alpha = 0.08f)
            ),
            shape = shape,
            color = MaterialTheme.colorScheme.surface
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).semantics {
                    contentDescription = searchHint
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(painterResource(R.drawable.ic_search), null, modifier = Modifier.size(18.dp))
                        Box(Modifier.weight(1f)) {
                            if (query.isEmpty()) {
                                Text(searchHint, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                            }
                            innerTextField()
                        }
                    }
                }
            )
        }
        Surface(
            onClick = {
                focusManager.clearFocus()
                onFilterClick()
            },
            modifier = Modifier.size(52.dp).shadow(
                5.dp, shape,
                ambientColor = PokedexShadow.copy(alpha = 0.04f),
                spotColor = PokedexShadow.copy(alpha = 0.08f)
            ).semantics {
                contentDescription = filterLabel
                selected = filtersExpanded || hasActiveFilters
            },
            shape = shape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.ic_filter), null, modifier = Modifier.size(20.dp))
                if (hasActiveFilters) {
                    Box(Modifier.align(Alignment.TopEnd).padding(8.dp).size(6.dp)
                        .background(MaterialTheme.colorScheme.onSurface, CircleShape))
                }
            }
        }
    }
}
