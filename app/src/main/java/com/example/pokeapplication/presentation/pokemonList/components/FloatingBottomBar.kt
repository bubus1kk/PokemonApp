package com.example.pokeapplication.presentation.pokemonlist.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.pokeapplication.R
import com.example.pokeapplication.ui.theme.PokedexShadow

@Composable
fun FloatingBottomBar(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(28.dp)
    Surface(
        modifier = modifier.shadow(
            12.dp, shape,
            ambientColor = PokedexShadow.copy(alpha = 0.04f),
            spotColor = PokedexShadow.copy(alpha = 0.08f)
        ),
        shape = shape,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.heightIn(min = 68.dp).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BottomBarItem(R.drawable.ic_home, stringResource(R.string.home_tab), true, Modifier.weight(1f))
            BottomBarItem(R.drawable.ic_favorites, stringResource(R.string.favorites_tab), false, Modifier.weight(1f))
            BottomBarItem(R.drawable.ic_types, stringResource(R.string.types_tab), false, Modifier.weight(1f))
            BottomBarItem(R.drawable.ic_profile, stringResource(R.string.profile_tab), false, Modifier.weight(1f))
        }
    }
}

@Composable
private fun BottomBarItem(
    @DrawableRes icon: Int,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    // Navigation is intentionally out of scope; these items expose their visual state only.
    Column(
        modifier = modifier
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                RoundedCornerShape(20.dp)
            )
            .semantics(mergeDescendants = true) {
                role = Role.Tab
                selected = isSelected
                if (!isSelected) disabled()
            }
            .padding(horizontal = 2.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(painterResource(icon), null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}
