package com.example.pokeapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.pokeapplication.presentation.pokemondetails.PokemonDetailsScreen
import com.example.pokeapplication.presentation.pokemonlist.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Serializable
data class FavoritesRoute(val onlyFavorites: Boolean = true)

@Serializable
data class PokemonDetailsRoute(val pokemonId: Int)

@Composable
fun PokemonNavHost() {
    val navController = rememberNavController()
    val onHomeClick: () -> Unit = {
        navController.navigate(HomeRoute) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
    val onFavoritesClick: () -> Unit = {
        navController.navigate(FavoritesRoute()) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
    val onPokemonClick: (Int) -> Unit = { pokemonId ->
        navController.navigate(PokemonDetailsRoute(pokemonId)) { launchSingleTop = true }
    }

    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(onPokemonClick = onPokemonClick,
                onHomeClick = onHomeClick, onFavoritesClick = onFavoritesClick)
        }
        composable<FavoritesRoute> {
            HomeScreen(onPokemonClick = onPokemonClick,
                onHomeClick = onHomeClick, onFavoritesClick = onFavoritesClick)
        }
        composable<PokemonDetailsRoute> {
            PokemonDetailsScreen(onBack = { navController.popBackStack() })
        }
    }
}
