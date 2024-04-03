package dev.jacobderynk.asterogram.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jacobderynk.asterogram.data.model.AsteroidEntity
import dev.jacobderynk.asterogram.data.model.CommunicationError
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
import okio.IOException
import timber.log.Timber
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val ITEMS_PER_PAGE = 10

class HomeViewModel(
    private val remoteRepository: AsteroidRemoteRepositoryImpl,
    private val localRepository: AsteroidLocalRepositoryImpl,
) : ViewModel() {

    private var currentOffset = 0

    private val _uiState = MutableStateFlow(HomeUiState(list = emptyList()))
    val uiState: StateFlow<HomeUiState> = _uiState

    private val bookmarkedAsteroids = localRepository.getSavedAsteroids()

    init {
        fetchAsteroids()
        combineBookmarkedAsteroids()
    }

    fun fetchAsteroids() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val result = withContext(Dispatchers.IO) {
                    remoteRepository.getAsteroids(ITEMS_PER_PAGE, currentOffset)
                }

                when (result) {
                    is CommunicationResult.Success -> {
                        Timber.d("✅ Got asteroids")
                        currentOffset += result.data.size
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                list = state.list + result.data.map { it.toDbModel() },
                            )
                        }
                    }

                    is CommunicationResult.Error -> {
                        Timber.e("❌ Error getting asteroids $result")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = HomeErrorState.ApiCommunicationError(result.error)
                            )
                        }
                    }

                    is CommunicationResult.Exception -> {
                        Timber.e("🔥 Exception getting asteroids $result")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = HomeErrorState.GenericError(result.exception)
                            )
                        }
                    }
                }
            } catch (exception: Exception) {
                Timber.e(exception, "💩 getAsteroids exception")
                when (exception) {
                    is UnknownHostException,
                    is SocketTimeoutException,
                    is ConnectException,
                    is NoRouteToHostException,
                    is IOException -> {
                        // internet error
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = HomeErrorState.ConnectionError(exception)
                            )
                        }
                    }
                    else -> {
                        // other issue
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = HomeErrorState.ConnectionError(exception)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun combineBookmarkedAsteroids() { // This solution is not the best but here we are..
        viewModelScope.launch {
            combine(_uiState, bookmarkedAsteroids) { uiState, bookmarked ->
                uiState.list.map { uiStateListItem ->
                    bookmarked.find { it.id == uiStateListItem.id }?.let { bookmarkedItem ->
                        uiStateListItem.copy(svg = bookmarkedItem.svg, isSaved = true)
                    } ?: uiStateListItem.copy(isSaved = false)
                }
            }.collect { resultList ->
                _uiState.update { it.copy(list = resultList) }
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

    fun resetErrorState() {
        _uiState.update { it.copy(error = null) }
    }

    @Immutable
    data class HomeUiState(
        val isLoading: Boolean = false,
        val list: List<AsteroidEntity>,
        val error: HomeErrorState? = null
    )

    sealed class HomeErrorState {
        data class ConnectionError(val throwable: Throwable?) : HomeErrorState()
        data class ApiCommunicationError(val communicationError: CommunicationError) : HomeErrorState()
        data class GenericError(val throwable: Throwable?) : HomeErrorState()
    }
}