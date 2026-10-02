package com.example.pokeapplication.presentation.pokemonlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pokeapplication.domain.model.Pokemon
import com.example.pokeapplication.ui.theme.PokedexShadow
import com.example.pokeapplication.ui.theme.pokemonTypeBackground
import java.util.Locale

@Composable
fun PokemonCard(pokemon: Pokemon, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val shape = RoundedCornerShape(22.dp)
    val imageBackground = pokemonTypeBackground(pokemon.types.firstOrNull()?.name)
    val displayName = pokemon.name.replaceFirstChar { it.titlecase(Locale.ROOT) }
    var imageLoaded by remember(pokemon.imageUrl) { mutableStateOf(false) }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().shadow(
            elevation = 4.dp, shape = shape,
            ambientColor = PokedexShadow.copy(alpha = 0.04f),
            spotColor = PokedexShadow.copy(alpha = 0.08f)
        ).semantics(mergeDescendants = true) {},
        shape = shape,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().aspectRatio(1.3f)
                    .clip(RoundedCornerShape(16.dp)).background(imageBackground),
                contentAlignment = Alignment.Center
            ) {
                if (!imageLoaded) {
                    Box(
                        modifier = Modifier.size(80.dp).background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.55f), CircleShape
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayName.take(1),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                if (pokemon.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        contentScale = ContentScale.Fit,
                        onSuccess = { imageLoaded = true },
                        onError = { imageLoaded = false },
                        onLoading = { imageLoaded = false }
                    )
                }
            }
            Column(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = displayName,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "#${pokemon.id.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                Text(
                    text = pokemon.types.joinToString(" · ") { it.name.uppercase(Locale.ROOT) },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
