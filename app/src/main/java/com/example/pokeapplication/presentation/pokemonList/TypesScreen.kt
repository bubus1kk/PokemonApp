package com.example.pokeapplication.presentation.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pokeapplication.R
import com.example.pokeapplication.presentation.pokemonlist.components.FloatingBottomBar
import com.example.pokeapplication.presentation.pokemonlist.components.pokemonTypes
import com.example.pokeapplication.ui.theme.pokemonTypeBackground
import java.util.Locale

@Composable
fun TypesScreen(
    onTypeClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(stringResource(R.string.types_tab),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground)
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 116.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pokemonTypes, key = { it }) { type ->
                    Surface(
                        onClick = { onTypeClick(type) },
                        shape = RoundedCornerShape(22.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Box(Modifier.padding(8.dp).fillMaxWidth()
                            .background(pokemonTypeBackground(type), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 30.dp),
                            contentAlignment = Alignment.Center) {
                            Text(type.replaceFirstChar { it.titlecase(Locale.ROOT) },
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
        FloatingBottomBar(
            isTypes = true,
            onHomeClick = onHomeClick,
            onFavoritesClick = onFavoritesClick,
            onTypesClick = {},
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp).fillMaxWidth()
        )
    }
}

