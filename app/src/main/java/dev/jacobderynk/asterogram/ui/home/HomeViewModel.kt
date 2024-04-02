package dev.jacobderynk.asterogram.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jacobderynk.asterogram.data.model.AsteroidEntity
import dev.jacobderynk.asterogram.data.model.CommunicationResult
import dev.jacobderynk.asterogram.data.repository.AsteroidLocalRepositoryImpl
import dev.jacobderynk.asterogram.data.repository.AsteroidRemoteRepositoryImpl
import dev.jacobderynk.asterogram.data.repository.toDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/* TODO:
    - liking posts
 */
class HomeViewModel(
    private val remoteRepository: AsteroidRemoteRepositoryImpl,
    private val localRepository: AsteroidLocalRepositoryImpl,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(list = emptyList()))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        fetchAsteroids()
        combineBookmarkedAsteroids()
    }

    fun fetchAsteroids() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val result = withContext(Dispatchers.IO) {
                    remoteRepository.getAsteroids(5, 0)
                }

                when (result) {
                    is CommunicationResult.Success -> {
                        Timber.d("✅ nice, we have $result")
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                list = result.data.map { it.toDbModel() },
                            )
                        }
                    }

                    else -> {
                        Timber.e("❌ oops $result")
                    }
                }
            } catch (exception: Exception) {
                Timber.e(exception, "💩 getAsteroids on init")
            }
        }
    }

    // This solution is not the best but here we are..
    private fun combineBookmarkedAsteroids() {
        viewModelScope.launch {
            combine(_uiState, localRepository.getSavedAsteroids()) { uiState, bookmarked ->
                val list = uiState.list.map { uiStateListItem ->
                    bookmarked.find { it.id == uiStateListItem.id }?.let { bookmarkedItem ->
                        uiStateListItem.copy(svg = bookmarkedItem.svg, isSaved = true)
                    } ?: uiStateListItem.copy(isSaved = false)
                }
                list
            }.collect { list ->
                _uiState.update { it.copy(list = list) }
            }
        }
    }

    fun bookmarkAsteroid(asteroid: AsteroidEntity, svgString: String?) {
        viewModelScope.launch {
            localRepository.insertAsteroid(
                asteroid.copy(
                    isSaved = true,
                    svg = svgString,
                )
            )
        }
    }

    fun removeBookmarkedAsteroid(asteroid: AsteroidEntity) {
        viewModelScope.launch {
            localRepository.delete(asteroid)
        }
    }

    @Immutable
    data class HomeUiState(
        val isLoading: Boolean = false,
        val list: List<AsteroidEntity>,
        val hasNetworkConnection: Boolean = true,
    )
}