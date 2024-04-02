package dev.jacobderynk.asterogram.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.jacobderynk.asterogram.R
import dev.jacobderynk.asterogram.ui.navigation.AsterogramNavGraph
import dev.jacobderynk.asterogram.ui.navigation.AsterogramNavigation
import dev.jacobderynk.asterogram.ui.navigation.AsterogramNavigationActions
import dev.jacobderynk.asterogram.ui.onboarding.OnboardingScreen
import dev.jacobderynk.asterogram.ui.theme.AsterogramTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsterogramApp(
    viewModel: MainActivityViewModel = koinViewModel()
) {

    AsterogramTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            AsterogramNavigationActions(navController)
        }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: AsterogramNavigation.HomeRoute.route
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        val showOnboarding by viewModel.showOnboarding.collectAsState()

        if (showOnboarding) {
            OnboardingScreen(
                finishOnboarding = viewModel::completeOnboarding,
                saveUsernameAction = viewModel::saveUsername,
            )
        } else {

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            scrolledContainerColor = MaterialTheme.colorScheme.surface,
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            when (currentRoute) {
                                AsterogramNavigation.HomeRoute.route -> {
                                    Icon(
                                        modifier = Modifier.height(40.dp),
                                        painter = painterResource(R.drawable.asterogram_logo),
                                        contentDescription = "Asterogram logo",
                                    )
                                }
                                AsterogramNavigation.ProfileRoute.route -> {
                                    Text(
                                        stringResource(R.string.profile),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                else -> { // In case somebody would forget to add name :D
                                    Text(
                                        currentRoute,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            if (currentRoute != AsterogramNavigation.HomeRoute.route) {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = { navigationActions.navigateToProfile.invoke() }) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },
            ) { innerPadding ->
                AsterogramNavGraph(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    navController = navController
                )
            }
        }
    }
}