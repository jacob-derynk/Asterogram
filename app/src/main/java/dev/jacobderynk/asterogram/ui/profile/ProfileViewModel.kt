package dev.jacobderynk.asterogram.ui.profile

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jacobderynk.asterogram.data.datastore.DatastoreRepositoryImpl
import dev.jacobderynk.asterogram.data.model.AsteroidEntity
import dev.jacobderynk.asterogram.data.repository.AsteroidLocalRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataStoreRepository: DatastoreRepositoryImpl,
    private val localRepository: AsteroidLocalRepositoryImpl,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(list = emptyList(), username = ""))
    val uiState: StateFlow<ProfileUiState> = _uiState

    var showOnboarding = mutableStateOf(false)
        private set

    init {
        fetchLocalAsteroids()
        observeUsername()
        viewModelScope.launch {
            showOnboarding.value = dataStoreRepository.getShowOnboarding()
        }
    }

    private fun fetchLocalAsteroids() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            localRepository.getSavedAsteroids().collect { list ->
                _uiState.update { it.copy(isLoading = false, list = list) }
            }
        }
    }

    fun saveUsername(username: String) {
        viewModelScope.launch {
            if (username.isNotBlank()) {
                dataStoreRepository.putUsername(username)
                _uiState.update { it.copy(showUsernameChangedToast = true) }
            }
        }
    }

    fun resetUsernameSaved() {
        _uiState.update { it.copy(showUsernameChangedToast = false) }
    }

    private fun observeUsername() {
        viewModelScope.launch {
            dataStoreRepository.getUsername().collect { username ->
                _uiState.update { it.copy(username = username) }
            }
        }
    }

    fun setShowOnboarding(value: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.putShowOnboarding(value)
            showOnboarding.value = value
        }
    }

    @Immutable
    data class ProfileUiState(
        val isLoading: Boolean = false,
        val list: List<AsteroidEntity>,
        val username: String,
        val showUsernameChangedToast: Boolean = false
    )
}