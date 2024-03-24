package dev.jacobderynk.asterogram.ui.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import dev.jacobderynk.asterogram.R

sealed class AsterogramNavigation(val route: String, @StringRes val stringRes: Int) {
    data object HomeRoute : AsterogramNavigation("home", R.string.home_route)
    data object ProfileRoute : AsterogramNavigation("profile", R.string.home_route)
}
class AsterogramNavigationActions(
    navController: NavController
) {
    val navigateHome: () -> Unit = {
        navController.navigate(AsterogramNavigation.HomeRoute.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToProfile: () -> Unit = {
        navController.navigate(AsterogramNavigation.ProfileRoute.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}