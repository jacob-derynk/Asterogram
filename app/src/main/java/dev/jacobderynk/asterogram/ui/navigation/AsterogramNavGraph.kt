package dev.jacobderynk.asterogram.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.jacobderynk.asterogram.ui.home.HomeScreen
import dev.jacobderynk.asterogram.ui.profile.ProfileScreen

@Composable
fun AsterogramNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AsterogramNavigation.HomeRoute.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn(tween(300)) },
        exitTransition = { fadeOut(tween(300)) },
        popEnterTransition = { fadeIn(tween(300)) },
        popExitTransition = { fadeOut(tween(300)) }

    ) {
        composable(
            route = AsterogramNavigation.HomeRoute.route,
        ) {
            HomeScreen()
        }
        composable(
            route = AsterogramNavigation.ProfileRoute.route,
        ) {
            ProfileScreen()
        }
    }

}