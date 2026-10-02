package com.example.pokeapplication.presentation.pokemondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.pokeapplication.R
import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.presentation.viewModel.PokemonDetailsViewModel
import com.example.pokeapplication.ui.theme.PokedexShadow
import com.example.pokeapplication.ui.theme.pokemonTypeBackground
import java.util.Locale

@Composable
fun PokemonDetailsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PokemonDetailsContent(
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::loadPokemon,
        onFavoriteClick = viewModel::onFavoriteClick,
        modifier = modifier
    )
}

@Composable
fun PokemonDetailsContent(
    uiState: PokemonDetailsUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
        Column(
            modifier = modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            DetailsToolbar(uiState, onBack, onFavoriteClick)
            when {
                uiState.isLoading -> {
                    val description = stringResource(R.string.home_loading)
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp).semantics { contentDescription = description },
                            strokeWidth = 2.dp
                        )
                    }
                }
                uiState.pokemon == null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            stringResource(if (uiState.error == PokemonDetailsError.LOAD)
                                R.string.details_load_error else R.string.details_not_found),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        TextButton(onClick = onRetry) { Text(stringResource(R.string.home_retry)) }
                    }
                }
                else -> {
                    val pokemon = uiState.pokemon
                    LazyColumn(
                        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item { PokemonHero(pokemon) }
                        item { PokemonHeading(pokemon) }
                        if (uiState.error == PokemonDetailsError.FAVORITE) {
                            item {
                                Text(stringResource(R.string.details_favorite_error),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error)
                            }
                        }
                        item { PokemonAbout(pokemon) }
                        if (pokemon.abilities.isNotEmpty()) {
                            item { PokemonAbilities(pokemon) }
                        }
                        if (pokemon.stats.isNotEmpty()) {
                            item { PokemonStats(pokemon) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailsToolbar(
    uiState: PokemonDetailsUiState,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onBack,
            modifier = Modifier.size(48.dp).shadow(4.dp, shape,
                ambientColor = PokedexShadow.copy(alpha = .04f),
                spotColor = PokedexShadow.copy(alpha = .08f)),
            shape = shape,
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.ic_back), stringResource(R.string.details_back),
                    modifier = Modifier.size(20.dp))
            }
        }
        val isFavorite = uiState.pokemon?.isFavorite == true
        Surface(
            modifier = Modifier.size(48.dp).shadow(4.dp, shape,
                ambientColor = PokedexShadow.copy(alpha = .04f),
                spotColor = PokedexShadow.copy(alpha = .08f)),
            shape = shape,
            color = if (isFavorite) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
        ) {
            IconToggleButton(
                checked = isFavorite,
                onCheckedChange = { onFavoriteClick() },
                enabled = uiState.pokemon != null && !uiState.isUpdatingFavorite && !uiState.isLoading
            ) {
                Icon(
                    painterResource(if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorites),
                    stringResource(if (isFavorite) R.string.details_remove_favorite else R.string.details_add_favorite),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun PokemonHero(pokemon: Pokemon) {
    var imageLoaded by remember(pokemon.imageUrl) { mutableStateOf(false) }
    val color = pokemonTypeBackground(pokemon.types.firstOrNull()?.name)
    Box(
        modifier = Modifier.fillMaxWidth().aspectRatio(343f / 300f)
            .clip(RoundedCornerShape(28.dp)).background(color),
        contentAlignment = Alignment.Center
    ) {
        if (!imageLoaded) {
            Box(
                modifier = Modifier.fillMaxWidth(.64f).aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = .55f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(pokemon.name.take(1).uppercase(Locale.ROOT),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 80.sp, lineHeight = 88.sp))
            }
        }
        if (pokemon.imageUrl.isNotBlank()) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(28.dp),
                contentScale = ContentScale.Fit,
                onSuccess = { imageLoaded = true },
                onLoading = { imageLoaded = false },
                onError = { imageLoaded = false }
            )
        }
        Text(
            text = "#${pokemon.id.toString().padStart(3, '0')}",
            modifier = Modifier.align(Alignment.TopEnd).padding(20.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PokemonHeading(pokemon: Pokemon) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(pokemon.name.asDisplayName(), style = MaterialTheme.typography.headlineMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            pokemon.types.forEach { type ->
                Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Text(type.name.uppercase(Locale.ROOT), modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun PokemonAbout(pokemon: Pokemon) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.details_about), style = MaterialTheme.typography.titleMedium)
        Surface(shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surface) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // PokéAPI stores height in decimetres and weight in hectograms.
                Measurement(stringResource(R.string.details_height),
                    stringResource(R.string.details_metres, pokemon.height / 10.0), Modifier.weight(1f))
                Measurement(stringResource(R.string.details_weight),
                    stringResource(R.string.details_kilograms, pokemon.weight / 10.0), Modifier.weight(1f))
                Measurement(stringResource(R.string.details_experience),
                    pokemon.baseExperience?.toString() ?: "—", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun Measurement(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun PokemonAbilities(pokemon: Pokemon) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.details_abilities), style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            pokemon.abilities.forEach { ability ->
                Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(ability.name.asDisplayName(), style = MaterialTheme.typography.bodyLarge)
                        if (ability.isHidden) {
                            Text(stringResource(R.string.details_hidden), style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonStats(pokemon: Pokemon) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.details_stats), style = MaterialTheme.typography.titleMedium)
        Surface(shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surface) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                pokemon.stats.forEach { stat ->
                    val label = when (stat.name) {
                        "hp" -> stringResource(R.string.details_hp)
                        "attack" -> stringResource(R.string.details_attack)
                        "defense" -> stringResource(R.string.details_defense)
                        "special-attack" -> stringResource(R.string.details_special_attack)
                        "special-defense" -> stringResource(R.string.details_special_defense)
                        "speed" -> stringResource(R.string.details_speed)
                        else -> stat.name.asDisplayName()
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(stat.value.toString(), style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

private fun String.asDisplayName(): String = replace('-', ' ').replaceFirstChar { it.titlecase(Locale.ROOT) }
