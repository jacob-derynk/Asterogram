package dev.jacobderynk.asterogram.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jacobderynk.asterogram.utils.NumbersFormatter
import dev.jacobderynk.asterogram.utils.generateSvgAsteroid
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(uiState.isLoading, { viewModel.fetchAsteroids() })
    val listState = rememberLazyListState()

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {

            if (uiState.error != null) {
                item {
                    HomeErrorDialog(
                        error = uiState.error!!,
                        onTryAgain = { viewModel.fetchAsteroids() },
                        onDismiss = { viewModel.resetErrorState() },
                    )
                }
            }

            items(
                items = uiState.list,
                key = { it.id }
            ) {

                val svgString: String by rememberSaveable {
                    mutableStateOf(generateSvgAsteroid())
                }

                PostCard(
                    bookmarked = it.isSaved,
                    onBookmarkClick = {
                        if (it.isSaved) {
                            viewModel.removeBookmarkedAsteroid(asteroid = it)
                        } else {
                            viewModel.bookmarkAsteroid(asteroid = it, svgString = svgString)
                        }
                    },
                    svgString = it.svg ?: svgString,
                    name = it.name,
                    year = it.year,
                    `class` = it.`class`,
                    mass = NumbersFormatter.formatMass(it.mass),
                    fall = it.fall
                )
            }
        }

        LoadMoreContentWhenScrolledToEnd(listState, viewModel::fetchAsteroids)
        PullRefreshIndicator(uiState.isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun LoadMoreContentWhenScrolledToEnd(
    listState: LazyListState,
    loadMoreAction: () -> Unit
) {
    val layoutInfo by remember { derivedStateOf { listState.layoutInfo } }
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    val lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: return

    if (lastVisibleItemIndex >= layoutInfo.totalItemsCount - 1) {
        LaunchedEffect(key1 = lastVisibleItemIndex) {
            loadMoreAction()
        }
    }
}