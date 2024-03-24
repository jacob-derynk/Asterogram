package dev.jacobderynk.asterogram.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jacobderynk.asterogram.data.model.CommunicationResult
import dev.jacobderynk.asterogram.data.repository.AsteroidRemoteRepositoryImpl
import dev.jacobderynk.asterogram.network.model.AsteroidResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeViewModel(
    private val remoteRepository: AsteroidRemoteRepositoryImpl,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(list = emptyList()))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        fetchAsteroids()
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
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                list = result.data,
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

    @Immutable
    data class  HomeUiState(
        val isLoading: Boolean = false,
        val list: List<AsteroidResponse>,
        val hasNetworkConnection: Boolean = true,
    )


}