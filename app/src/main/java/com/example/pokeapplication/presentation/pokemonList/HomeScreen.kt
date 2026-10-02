package com.example.pokeapplication.presentation.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokeapplication.R
import com.example.pokeapplication.domain.model.PokemonSortOrder
import com.example.pokeapplication.presentation.pokemonlist.components.PokemonFiltersPanel
import com.example.pokeapplication.presentation.pokemonlist.components.FloatingBottomBar
import com.example.pokeapplication.presentation.pokemonlist.components.PokemonCard
import com.example.pokeapplication.presentation.pokemonlist.components.PokemonSearchBar
import com.example.pokeapplication.presentation.viewModel.PokemonListViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onQueryChanged = viewModel::onQueryChanged,
        onRetry = viewModel::onRefresh,
        onPokemonClick = onPokemonClick,
        onTypeChanged = viewModel::onTypeChanged,
        onSortOrderChanged = viewModel::onSortOrderChanged,
        onResetFilters = {
            if (uiState.typeName != null) viewModel.onTypeChanged(null)
            if (uiState.sortOrder != PokemonSortOrder.ID_ASC) viewModel.onSortOrderChanged(PokemonSortOrder.ID_ASC)
        },
        onHomeClick = onHomeClick,
        onFavoritesClick = onFavoritesClick,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    uiState: PokemonListUiState,
    onQueryChanged: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onPokemonClick: (Int) -> Unit = {},
    onTypeChanged: (String?) -> Unit = {},
    onSortOrderChanged: (PokemonSortOrder) -> Unit = {},
    onResetFilters: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {}
) {
    var filtersExpanded by rememberSaveable { mutableStateOf(false) }
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    fun scrollToStart() {
        scope.launch { gridState.scrollToItem(0) }
    }
    val hasActiveFilters = uiState.typeName != null || uiState.sortOrder != PokemonSortOrder.ID_ASC
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
    ) {
        Column(Modifier.fillMaxSize()) {
            HomeHeader(
                isFavorites = uiState.onlyFavorites,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)
            )
            PokemonSearchBar(
                query = uiState.query,
                onQueryChanged = {
                    onQueryChanged(it)
                    scrollToStart()
                },
                onFilterClick = {
                    filtersExpanded = !filtersExpanded
                    scrollToStart()
                },
                filtersExpanded = filtersExpanded,
                hasActiveFilters = hasActiveFilters,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            )
            PokemonGrid(
                gridState = gridState,
                uiState = uiState,
                onRetry = onRetry,
                onPokemonClick = onPokemonClick,
                filtersExpanded = filtersExpanded,
                onTypeChanged = { onTypeChanged(it); scrollToStart() },
                onSortOrderChanged = { onSortOrderChanged(it); scrollToStart() },
                onResetFilters = { onResetFilters(); scrollToStart() },
                modifier = Modifier.weight(1f)
            )
        }

        FloatingBottomBar(
            isFavorites = uiState.onlyFavorites,
            onHomeClick = onHomeClick,
            onFavoritesClick = onFavoritesClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun HomeHeader(isFavorites: Boolean, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(if (isFavorites) R.string.favorites_tab else R.string.home_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(if (isFavorites) R.string.favorites_subtitle else R.string.home_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PokemonGrid(
    gridState: LazyGridState,
    uiState: PokemonListUiState,
    onRetry: () -> Unit,
    onPokemonClick: (Int) -> Unit,
    filtersExpanded: Boolean,
    onTypeChanged: (String?) -> Unit,
    onSortOrderChanged: (PokemonSortOrder) -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 116.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (filtersExpanded) {
            item(key = "filters", span = { GridItemSpan(maxLineSpan) }) {
                PokemonFiltersPanel(
                    typeName = uiState.typeName,
                    sortOrder = uiState.sortOrder,
                    onTypeChanged = onTypeChanged,
                    onSortOrderChanged = onSortOrderChanged,
                    onReset = onResetFilters,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
        if (uiState.isLoading) {
            item(key = "loading", span = { GridItemSpan(maxLineSpan) }) {
                val loadingDescription = stringResource(R.string.home_loading)
                Box(Modifier.fillMaxWidth().padding(12.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp).semantics {
                            contentDescription = loadingDescription
                        },
                        strokeWidth = 2.dp
                    )
                }
            }
        }
        if (uiState.errorMessage != null) {
            item(key = "error", span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = uiState.errorMessage,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = onRetry, enabled = !uiState.isLoading) {
                        Text(stringResource(R.string.home_retry), color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
        if (uiState.pokemons.isEmpty() && !uiState.isLoading && uiState.errorMessage == null) {
            item(key = "empty", span = { GridItemSpan(maxLineSpan) }) {
                Box(Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(if (uiState.onlyFavorites && uiState.query.isBlank() && uiState.typeName == null)
                            R.string.favorites_empty else R.string.home_no_results),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        items(uiState.pokemons, key = { it.id }, contentType = { "pokemon" }) { pokemon ->
            PokemonCard(pokemon = pokemon, onClick = { onPokemonClick(pokemon.id) })
        }
    }
}
