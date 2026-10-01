package com.example.pokeapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokeapplication.presentation.pokemondetails.PokemonDetailsScreen
import com.example.pokeapplication.presentation.pokemonlist.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Serializable
data class PokemonDetailsRoute(val pokemonId: Int)

@Composable
fun PokemonNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(onPokemonClick = { pokemonId ->
                navController.navigate(PokemonDetailsRoute(pokemonId)) {
                    launchSingleTop = true
                }
            })
        }
        composable<PokemonDetailsRoute> {
            PokemonDetailsScreen(onBack = { navController.popBackStack() })
        }
    }
}
