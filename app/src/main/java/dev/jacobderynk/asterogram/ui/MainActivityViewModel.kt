package dev.jacobderynk.asterogram.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jacobderynk.asterogram.data.datastore.DatastoreRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    private val dataStoreRepository: DatastoreRepositoryImpl,
) : ViewModel() {

    var showOnboarding = MutableStateFlow(false)
        private set

    init {
        runBlocking {
            showOnboarding.update {  dataStoreRepository.getShowOnboarding() }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            dataStoreRepository.putShowOnboarding(false)
            showOnboarding.update { false }
        }
    }

    fun saveUsername(username: String) {
        viewModelScope.launch {
            if (username.isEmpty()) {
                dataStoreRepository.putUsername(generateUsername())
            } else {
                dataStoreRepository.putUsername(username)
            }
        }
    }

    private fun generateUsername(): String {
        val usernames = listOf(
            "Elon Must",
            "E.T.",
            "Spaghetti monster",
            "PlutoNotAPlanet",
            "Darth Vaper",
            "Nyan Cat"
        )
        return usernames.random()
    }

}